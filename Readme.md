# TelemetryAgent

## A Production-Oriented Java Telemetry Collection Agent

TelemetryAgent is a lightweight, extensible system monitoring agent built from scratch in Java.

The agent runs on client machines, collects system-level telemetry such as CPU, memory, disk, and future network metrics, and is designed to securely communicate with a centralized backend for monitoring and analysis.

The project focuses on building a production-grade observability component with emphasis on:

- Clean Architecture
- SOLID Principles
- Modular Design
- Platform Abstraction
- Extensible Telemetry Pipelines
- Secure Agent Communication

The long-term vision is to evolve this agent into a high-performance Rust-based telemetry collector while maintaining a Spring Boot-based monitoring backend.

---

# Overview

Modern observability platforms rely on lightweight agents deployed close to the infrastructure they monitor.

TelemetryAgent aims to provide a scalable agent architecture capable of:

- Collecting system-level metrics
- Managing agent lifecycle
- Scheduling periodic telemetry collection
- Supporting multiple operating systems
- Securely identifying registered agents
- Reliably reporting telemetry data

---

# Architecture

TelemetryAgent follows a modular telemetry pipeline.

The execution flow:

Configuration  
→ Agent Lifecycle  
→ Scheduler  
→ Collector Framework  
→ Platform Providers  
→ Metric Pipeline  
→ Reporter Layer  
→ Backend

Each stage has a clearly defined responsibility and communicates through abstractions.

This allows new collectors, operating systems, and reporting mechanisms to be added without modifying existing components.

---

# Core Features Implemented

## Configuration Framework

A flexible configuration pipeline was implemented to support multiple configuration sources.

Supported sources:

- Command-line arguments
- Properties files
- Default configuration values

Implemented components:

- ConfigurationSource abstraction
- PropertiesConfigurationSource
- ArgumentsConfigurationSource
- DefaultConfigurationSource
- ConfigurationProvider
- ConfigurationValidator

Design goals:

- Avoid hardcoded configuration
- Support future configuration sources
- Keep configuration logic independent from agent execution

Patterns used:

- Strategy Pattern
- Dependency Inversion

---

# Agent Lifecycle Management

TelemetryAgent includes a complete lifecycle management system.

Implemented capabilities:

- Agent initialization
- Agent startup
- Runtime state management
- Graceful shutdown
- JVM shutdown hook handling

Lifecycle states:

- NEW
- STARTING
- RUNNING
- STOPPING
- STOPPED
- FAILED

The lifecycle abstraction ensures the agent can safely manage its runtime state and resources.

---

# Scheduler Framework

A scheduler abstraction was implemented for periodic telemetry execution.

Built using:

- Java ScheduledExecutorService

Responsibilities:

- Schedule telemetry collection tasks
- Execute collectors periodically
- Manage scheduler lifecycle
- Support graceful shutdown

The scheduler is independent of collectors, allowing different execution strategies in the future.

---

# Extensible Collector Architecture

TelemetryAgent uses a modular collector-based design.

Collectors are responsible for:

- Requesting system information
- Processing collected data
- Producing standardized telemetry metrics

Currently implemented collectors:

## CPU Collector

Collects CPU-related telemetry.

## Memory Collector

Collects memory utilization information.

## Disk Collector

Collects filesystem usage metrics.

Future collectors:

- Network Collector
- Process Collector
- JVM Collector
- Application Metrics Collector

The collector architecture follows the Open/Closed Principle, allowing new telemetry sources to be introduced without modifying existing components.

---

# Platform Abstraction Layer

TelemetryAgent separates telemetry collection logic from operating system implementations.

Architecture:

Collector  
↓  
Platform Provider  
↓  
Operating System

This prevents collectors from being tightly coupled to a specific operating system.

Implemented platform providers:

## CPU Provider

Uses:

- OperatingSystemMXBean

Responsible for CPU-related system information.

---

## Memory Provider

Uses:

- OperatingSystemMXBean

Responsible for:

- Total memory
- Used memory
- Available memory

---

## Disk Provider

Uses:

- Java NIO FileStore

Responsible for:

- Total disk space
- Available disk space

---

# Metric Pipeline

A standardized telemetry model was introduced.

Each metric contains:

- Metric name
- Value
- Unit
- Timestamp
- Additional metadata

Example metrics:

- CPU usage percentage
- Memory utilization
- Disk availability

This provides a common format for future reporters and backend ingestion.

---

# Security Architecture

TelemetryAgent is being integrated with a Spring Boot backend.

The security model separates:

## User Authentication

Human users authenticate using:

- Email/password
- BCrypt password hashing
- JWT authentication

Authentication flow:

User Credentials  
→ AuthenticationManager  
→ UserDetailsService  
→ Password Verification  
→ JWT Generation  
→ Security Context


---

## Agent Authentication

Agents require their own identity mechanism.

Instead of using user credentials, agents authenticate using Personal Access Tokens (PAT).

PAT lifecycle:

1. Generate cryptographically secure token
2. Hash token before storage
3. Return raw token only once
4. Validate future requests using hash comparison

This follows the same principles used for API keys and service credentials.

---

# Design Principles

TelemetryAgent follows strong software engineering principles.

## SOLID Principles

### Single Responsibility Principle

Each component has one clear responsibility.

Examples:

- Scheduler handles scheduling
- Collector handles telemetry collection
- Provider handles OS interaction
- Reporter handles telemetry delivery


### Open/Closed Principle

The system is open for extension and closed for modification.

Examples:

Adding:

- NetworkCollector
- New OS provider
- New reporter

does not require changing existing components.


### Dependency Inversion Principle

Core modules depend on abstractions instead of concrete implementations.

Example:

Agent depends on:

- Scheduler interface

instead of:

- DefaultScheduler implementation

---

# Design Patterns Used

## Command Pattern

Used for CLI command execution.

Provides an extensible command framework.

---

## Strategy Pattern

Used for:

- Configuration sources
- Platform providers

---

## Registry Pattern

Used for command registration and lookup.

---

## Dependency Injection

Used to maintain loose coupling between modules.

---

# Current Implementation Status

Completed:

- CLI command framework
- Configuration pipeline
- Configuration validation
- Agent lifecycle management
- Graceful shutdown handling
- Scheduler framework
- Metric model
- Collector abstraction
- CPU telemetry collection
- Memory telemetry collection
- Disk telemetry collection
- Platform provider abstraction
- Backend authentication foundation
- Agent credential generation design

---

# Roadmap

## Telemetry Reporting

Upcoming:

- Reporter abstraction
- Console reporter
- File reporter
- HTTP reporter

---

## Backend Communication

Upcoming:

- Agent registration API
- PAT authentication
- Heartbeat service
- HTTP communication
- Telemetry ingestion pipeline

---

## Reliability

Upcoming:

- Offline telemetry buffering
- Retry mechanism
- Local persistence
- Failure recovery

---

## Advanced Monitoring

Future:

- Network telemetry
- Process monitoring
- Remote command execution
- Plugin-based collectors
- Container deployment
- Rust-based agent implementation

---

# Technology Stack

## Agent

- Java 21
- Maven
- ScheduledExecutorService
- Java Management Extensions (JMX)
- Java NIO
- Docker

## Backend

- Spring Boot
- Spring Security
- JWT
- PostgreSQL
- Redis

---

# Current Status

TelemetryAgent has completed the core monitoring engine.

The current implementation is capable of:

- Loading configuration
- Managing agent lifecycle
- Scheduling telemetry tasks
- Collecting system metrics
- Producing standardized telemetry objects

The next major milestone is enabling secure communication between agents and backend through:

- Agent registration
- PAT authentication
- Heartbeat communication
- Telemetry reporting