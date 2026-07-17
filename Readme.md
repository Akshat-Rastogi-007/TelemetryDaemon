# TelemetryDaemon

TelemetryDaemon is a lightweight, extensible system monitoring agent built in Java.

The agent runs on client machines, collects system telemetry such as CPU, memory, disk, and (future) network metrics, and is designed to periodically send them to a centralized backend for monitoring and analysis.

This project is being built from scratch with a strong focus on clean architecture, SOLID principles, object-oriented design, and production-quality engineering practices. The long-term goal is to rewrite the agent in Rust while keeping the backend in Spring Boot.

---

# 🚀 Vision

TelemetryDaemon aims to become a production-ready monitoring agent capable of:

- System resource monitoring
- Secure agent registration
- Periodic heartbeat scheduling
- Modular telemetry collection
- Remote command execution
- Offline telemetry buffering
- Automatic retry and recovery
- Plugin-based collector architecture
- Cross-platform support (Linux, Windows, macOS)

---

# 🏗️ Current Progress

## ✅ Implemented

### CLI & Configuration

- CLI command parser
- Command registry
- Command abstraction
- Configuration loading pipeline
- Configuration validation
- Default configuration support
- Properties file support
- Command-line argument overrides

### Agent Lifecycle

- Agent abstraction
- Agent lifecycle management
- Agent launcher
- Graceful shutdown hook
- Scheduler integration

### Telemetry Collection Framework

- Collector abstraction
- Metric model
- Snapshot-based platform architecture

### Platform Providers

- CPU Provider
- Memory Provider
- Disk Provider

### Collectors

- CPU Collector
- Memory Collector
- Disk Collector

### Scheduler

- Scheduled telemetry collection
- File-based telemetry logging
- Structured telemetry log output

---

## 🚧 In Progress

- Platform abstraction improvements
- Reporter pipeline
- Console reporter
- File reporter

---

## 📅 Planned

### Platform

- Advanced Network telemetry
- Linux-specific metrics
- Windows platform implementation
- macOS platform implementation

### Agent

- Registration service
- Heartbeat service
- HTTP client

### Telemetry

- Offline telemetry queue
- Retry mechanism
- Local persistence
- HTTP reporter

### Future

- Remote command execution
- Plugin architecture
- Docker deployment
- Rust implementation of the agent

---

# 🏛️ Architecture

```
                  Operating System
                          │
                          ▼
                    Platform Provider
                          │
                          ▼
                      Snapshot Model
                          │
                          ▼
                       Collector
                          │
                          ▼
                    Collection<Metric>
                          │
                          ▼
                      Reporter (Planned)
                          │
                          ▼
                         Backend
```

---

# 📂 Current Architecture

```
agent
├── command
├── config
├── lifecycle
├── collector
│   ├── cpu
│   ├── memory
│   └── disk
├── platform
│   ├── cpu
│   ├── memory
│   └── disk
├── scheduler
├── telemetry
└── launcher
```

---

# 🛠️ Tech Stack

## Current

- Java 21
- Java CLI
- ScheduledExecutorService
- OperatingSystemMXBean
- Java NIO FileStore
- Docker (Development)

## Planned

- Spring Boot Backend
- PostgreSQL
- Redis
- Docker
- Rust Agent

---

# 🎯 Design Principles

This project emphasizes:

- Clean Architecture
- SOLID Principles
- Separation of Concerns
- Dependency Injection
- Strategy Pattern
- Registry Pattern
- Composition over Inheritance
- Platform-independent abstractions

---

# 📌 Project Status

This project is under active development.

The focus is currently on building a robust and extensible telemetry collection framework before introducing networking and backend communication.

---

# 🗺️ Roadmap

## Phase 1 — Foundation ✅

- [x] Project initialization
- [x] CLI command parser
- [x] Configuration pipeline
- [x] Agent lifecycle
- [x] Scheduler
- [x] Metric model
- [x] Collector framework

---

## Phase 2 — Platform Telemetry 🚧

- [x] CPU Provider & Collector
- [x] Memory Provider & Collector
- [x] Disk Provider & Collector
- [ ] Network Provider & Collector
- [ ] Platform implementations

---

## Phase 3 — Reporting

- [ ] Reporter abstraction
- [ ] Console reporter
- [ ] File reporter
- [ ] HTTP reporter

---

## Phase 4 — Communication

- [ ] Registration service
- [ ] Heartbeat service
- [ ] HTTP client
- [ ] Backend communication

---

## Phase 5 — Reliability

- [ ] Offline queue
- [ ] Retry mechanism
- [ ] Local persistence
- [ ] Recovery

---

## Phase 6 — Advanced Features

- [ ] Remote command execution
- [ ] Plugin architecture
- [ ] Docker deployment
- [ ] Rust implementation