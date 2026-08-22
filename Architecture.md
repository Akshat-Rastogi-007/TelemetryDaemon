# Telemetry Architecture

Telemetry consists of two independent applications.

```
+--------------------+
|  Dashboard Client  |
+---------+----------+
          |
          | JWT
          |
          ▼
+--------------------------+
|     Telemetry Server      |
+--------------------------+
          ▲
          |
          | Agent Token
          |
+---------+----------+
|   Telemetry Agent   |
+--------------------+
          ▲
          |
          | PAT
          |
   Agent Registration
```

---

# Authentication

Telemetry intentionally separates user and agent identities.

## JWT

Used by dashboard users.

```
Dashboard User
      │
      ▼
Login
      │
      ▼
JWT
      │
      ▼
Dashboard APIs
```

---

## Personal Access Token

Used only while registering a new agent.

```
Dashboard User
      │
      ▼
Generate PAT
      │
      ▼
Register Agent
```

---

## Agent Token

Used after registration.

```
Telemetry Agent
      │
      ▼
Agent Token
      │
      ▼
Telemetry APIs
```

---

# Spring Security

```
                           Incoming Request
                                   │
      ┌──────────────┬─────────────┼──────────────┬──────────────┐
      ▼              ▼             ▼              ▼
   Public          PAT            JWT        Agent Token
      │              │             │              │
      │         PatFilter     JwtFilter     AgentFilter
      │              │             │              │
      ▼              ▼             ▼              ▼
 Public APIs   Registration   Dashboard     Telemetry
```

---

# Agent Architecture

```
Configuration

↓

Identity

↓

Scheduler

↓

Collectors

↓

Telemetry Batch

↓

Serializer

↓

HTTP Transport

↓

Telemetry Server
```

---

# Server Architecture

```
Authentication

↓

Controller

↓

Application Service

↓

Repository

↓

Database / Cache
```

---

# Module Structure

```
TelemetryServer
│
├── agent
│
├── telemetry
│
├── security
│
├── user
│
├── common
│
└── configuration
```

---

# Design Principles

- Spring Modulith
- SOLID Principles
- Layered Architecture
- Separation of Authentication Mechanisms
- Extensible Collector Pipeline
- Transport Abstraction
- Clean Domain Boundaries

---

# Telemetry Batch Pipeline

End-to-end class-level flow of a single telemetry batch, from metric
collection on the agent to persistence on the server.

## Agent Side — Happy Path (Connected)

```
CpuProvider / MemoryProvider / DiskProvider          ← Platform layer
              │
              │  snapshot()
              ▼
CpuCollector / MemoryCollector / DiskCollector       ← implements Collector
              │
              │  collect() → Collection<Metric>
              ▼
     CollectorManager                                ← holds Map<id, CollectorRegister>
              │
              │  getCollectors()
              ▼
     CollectorScheduler                              ← iterates enabled registers
              │
              │  schedule(collector, reporters, interval)
              ▼
     DefaultSchedular                                ← ScheduledExecutorService
              │
              │  scheduleAtFixedRate(...)
              │    1. collector.collect() → metrics
              │    2. new TelemetryBatch(id, now, metrics)
              │    3. reporter.report(batch)
              ▼
     HttpReporter                                    ← implements Reporter
              │
              │  report(batch)
              ▼
     TelemetryDispatcher
              │
              │  checks ConnectionStateManager
              │  state == CONNECTED ?
              │
              ▼  YES
     HttpTransport                                   ← implements TelemetryTransport
              │
              │  1. JacksonTelemetrySerializer.serialize(batch) → JSON
              │  2. IdentityService.getIdentity().getAgentToken()
              │  3. HttpClient.send(POST /app/agent/telemetry/submit/)
              ▼
     Telemetry Server
```

## Agent Side — Offline Path (Disconnected / Transport Failure)

```
     TelemetryDispatcher
              │
              │  state == DISCONNECTED  —OR—  HttpTransport throws TransportException
              ▼
     TransportBufferPipeline
              │
              │  1. BufferPolicy.canStore(currentSize)?
              │     YES → TransportBuffer.add(batch)
              │     NO  → TransportBuffer.notifyAllObservers()
              │            └─→ TelemetryFlusher.onBufferFull()
              │                   └─→ TransportBuffer.removeOldest()
              │            then TransportBuffer.add(batch)
              ▼
     DefaultTransportBuffer                          ← ConcurrentLinkedQueue<TelemetryBatch>
              │
              │  (batches sit here until connection is restored)
              │
              ▼  ConnectionStateManager.markConnected()
              │  notifies observers
              ▼
     TelemetryFlusher                                ← implements ConnectionStateObserver
              │
              │  onStateChanged(CONNECTED)
              │    for each pending batch:
              │      TelemetryTransport.send(batch)
              │      TransportBuffer.remove(batch)
              ▼
     HttpTransport → Telemetry Server
```

## Server Side

```
     HTTP POST /app/agent/telemetry/submit/
              │
              │  AgentFilter authenticates Agent Token
              ▼
     AgentTelemetryController
              │
              │  submitTelemetryData(TelemetryBatchRequest)
              ▼
     AgentTelemetryService
              │
              │  1. mapToTelemetryBatch(request) → TelemetryBatch domain
              │  2. CurrentAgentProvider.currentAgent().getId()
              │  3. LatestMetricsRepository.save(agentId, batch)
              ▼
     InMemoryLatestMetricsRepository                 ← ConcurrentHashMap<Long, TelemetryBatch>
```

## Key Classes Summary

| Stage              | Class                         | Role                                                  |
| ------------------ | ----------------------------- | ----------------------------------------------------- |
| Platform           | `CpuProvider`, `MemoryProvider`, `DiskProvider` | Read OS-level metrics via MXBean / FileStore |
| Collection         | `CpuCollector`, `MemoryCollector`, `DiskCollector` | Convert snapshots into `Collection<Metric>` |
| Registration       | `CollectorManager`            | Registry of enabled/disabled collectors               |
| Scheduling         | `CollectorScheduler` → `DefaultSchedular` | Periodic fixed-rate execution                |
| Batch creation     | `TelemetryBatch`              | Groups collector id + timestamp + metrics             |
| Reporting          | `HttpReporter`                | Delegates batch to dispatcher                         |
| Dispatching        | `TelemetryDispatcher`         | Routes to transport or buffer based on connection     |
| Serialization      | `JacksonTelemetrySerializer`  | `TelemetryBatch` → JSON via Jackson                   |
| Transport          | `HttpTransport`               | Sends JSON over HTTP with Agent Token auth            |
| Buffering          | `TransportBufferPipeline` + `DefaultTransportBuffer` | Stores batches while offline     |
| Buffer policy      | `DefaultBufferPolicy`         | Max-size guard before eviction                        |
| Flush on reconnect | `TelemetryFlusher`            | Observer; drains buffer when connection restored      |
| Connection mgmt    | `ConnectionStateManager`      | Tracks CONNECTED / DISCONNECTED, notifies observers   |
| Server controller  | `AgentTelemetryController`    | REST endpoint receiving the batch                     |
| Server service     | `AgentTelemetryService`       | Maps request DTO → domain, identifies agent, persists |
| Server repository  | `InMemoryLatestMetricsRepository` | Stores latest batch per agent in memory           |