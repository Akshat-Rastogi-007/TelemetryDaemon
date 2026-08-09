![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![License](https://img.shields.io/badge/License-MIT-blue)

# Telemetry

> A lightweight, secure and extensible telemetry platform built with Java and Spring Boot.

Telemetry is a self-hosted observability platform consisting of two independent applications:

- **Telemetry Agent** – Collects system metrics and securely sends them to the server.
- **Telemetry Server** – Authenticates agents, ingests telemetry, stores metrics, and exposes APIs for dashboards.

The project is designed using **Spring Modulith**, **Clean Architecture**, and **SOLID Principles**.

---

## Features

### Agent

- Configuration Loading Pipeline
- Identity Persistence
- Agent Registration
- Secure Authentication
- Scheduled Metric Collection
- Collector Architecture
- HTTP Transport
- JSON Serialization

### Server

- JWT Authentication
- Personal Access Token (PAT) Authentication
- Agent Token Authentication
- Multiple Spring Security Filter Chains
- Telemetry Ingestion
- Dashboard APIs
- Latest Metrics Cache

---

## Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Modulith
- Spring Data JPA
- Hibernate

### Database

- MySQL

### Build Tool

- Maven

---

## Documentation

- 📘 [Architecture](ARCHITECTURE.md)
- 📙 [REST API](API.md)

---

## Project Status

Current Version

```
v1.0.0
```

---

## Roadmap

### v1.0

- Authentication
- Agent Registration
- Telemetry Ingestion
- Identity Persistence

### v1.1

- Dashboard
- Charts
- Telemetry History

### v2.0

- Kafka
- Redis
- WebSockets
- Alert Engine

---

## License

MIT