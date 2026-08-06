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