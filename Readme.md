# TelemetryDaemon

TelemetryDaemon is a lightweight, extensible system monitoring agent built in Java.

The agent runs on client machines, collects telemetry such as CPU, memory, disk, and network usage, and periodically sends it to a centralized backend for monitoring and analysis.

This project is being built from scratch with a strong focus on clean architecture, object-oriented design, and extensibility. The long-term goal is to rewrite the agent in Rust while keeping the backend in Spring Boot.

---

## 🚀 Vision

TelemetryDaemon aims to become a production-ready monitoring agent capable of:

- System resource monitoring
- Secure agent registration
- Periodic heartbeat mechanism
- Telemetry collection
- Remote command execution
- Offline telemetry buffering
- Automatic retry and recovery
- Plugin-based collector architecture

---

## 🏗️ Current Progress

### ✅ Implemented

- CLI command parser
- Command abstraction
- Configuration loading pipeline
- Configuration validation
- Properties file support
- Command-line argument overrides

### 🚧 In Progress

- Agent lifecycle
- Registration service
- Heartbeat scheduler

### 📅 Planned

- HTTP client
- CPU collector
- Memory collector
- Disk collector
- Network collector
- Local telemetry queue
- Offline mode
- Remote command execution
- Docker deployment
- Rust implementation of the agent

---

## 🏛️ Architecture

```
CLI
 │
 ▼
Command Parser
 │
 ▼
Commands
 │
 ▼
Configuration Loader
 │
 ▼
Agent Service
 │
 ├── Registration
 ├── Scheduler
 ├── Collectors
 └── Heartbeat
```

---

## 🛠️ Tech Stack

### Current

- Java
- Java CLI
- Docker (development)

### Planned

- Spring Boot (Backend)
- PostgreSQL
- Redis
- Docker
- Rust (Agent rewrite)

---

## 🎯 Goals

- Learn systems programming concepts
- Design a production-style monitoring agent
- Practice clean architecture and SOLID principles
- Explore distributed systems and observability
- Rewrite the Java agent in Rust

---

## 📌 Project Status

This project is under active development.

The architecture and features will continue to evolve as the project grows.


## Roadmap

- [x] Project initialization
- [x] CLI command parser
- [x] Configuration loading
- [ ] Agent lifecycle
- [ ] Registration service
- [ ] Heartbeat service
- [ ] CPU collector
- [ ] Memory collector
- [ ] Disk collector
- [ ] Network collector
- [ ] HTTP client
- [ ] Offline queue
- [ ] Docker deployment
- [ ] Rust implementation