# Telemetry Server — Developer Guide

> Snapshot: 2026-10-29. Back to the overview: [`../PROJECT_STATUS.md`](../PROJECT_STATUS.md)
> REST reference with request bodies: [`../API.md`](../API.md)

Spring Boot **3.5.3**, Java 21, Spring Security, Spring Data JPA (MySQL/MariaDB), **Spring
Modulith** 1.4.4, jjwt 0.12.7, ModelMapper, Lombok, springdoc (Swagger UI). The POM also pulls in
the Kotlin compiler plugin (there is no Kotlin code — leftover from IntelliJ; the Maven compiler
setup is customised to run Kotlin + Java together).

Base package: `com.telemtry.telemetryserver` (note the spelling **telemtry**).

---

## 1. Build & run

```bash
cd TelemetryServer
cp src/main/resources/application.properties.example src/main/resources/application.properties
./mvnw spring-boot:run          # http://localhost:8080
```

`application.properties` is git-ignored (holds DB password + JWT key). Required values:

| Property | Notes |
|---|---|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/telemetry_db` — ⚠ the `.example` file says `c:mysql://`, fix it |
| `spring.datasource.username/password` | |
| `security.jwt.key` | **Must be Base64** — `JwtService` does `Decoders.BASE64.decode(key)` and HS256 needs ≥ 256 bits. Use `openssl rand -base64 48` |
| `security.jwt.expiration` | ms, default 3600000 (1 h) |
| `spring.jpa.hibernate.ddl-auto=update` | Schema is auto-created; no migrations (Flyway/Liquibase) |

Both `mysql-connector-j` and `mariadb-java-client` are on the classpath; the driver class set is MySQL's.

Swagger UI (springdoc): `http://localhost:8080/swagger-ui.html` — note it is **not** listed in any
security chain's matcher, so check whether it's reachable before relying on it.

Tests: `src/test/.../TelemetryServerApplicationTests` has `contextLoads()` and a Modulith
`ApplicationModules.verify()` test. `TelemetryServer/src/test` is in the root `.gitignore`, so
tests are local-only. `contextLoads` needs a running DB.

---

## 2. Module structure (Spring Modulith)

Each top-level package under the base package is a Modulith "application module", laid out in
**api / application / domain / infrastructure** layers (hexagonal-ish):

```
com.telemtry.telemetryserver
├── TelemetryServerApplication
├── common/          OPEN module (anyone may use it)
│   ├── domain/ApiResponseDto<T>          { data, statusCode, message } — every response uses it
│   ├── exception/                        custom RuntimeExceptions + GlobalExceptionHandler
│   └── infrastructure/
│       ├── hash/Sha256Hasher             ("sha256Hasher") hex SHA-256 of tokens
│       ├── secureKeyGenerator/SecureRandomKeyGenerator  32 random bytes, Base64-URL, no padding
│       └── MapperConfiguration           ModelMapper bean
├── user/            users + personal access tokens
│   ├── api/                 ← NamedInterface "api": CurrentUserProvider, PatAuthenticator, controllers, DTOs
│   ├── application/user/UserService, AccountStatus
│   ├── application/pat/PersonalAccessTokenService
│   ├── domain/model/User, PersonalAccessToken, Roles
│   ├── domain/repository/UserRepository, PersonalAccessTokenRepository   (ports)
│   └── infrastructure/
│       ├── repository/…  JpaUserRepository ("jpaUserRepository"), JpaPatRepository + Spring Data interfaces (adapters)
│       └── security/user/  CurrentUser (UserDetails), UserDetailsServiceImpl, UserSecurityCurrentUserProvider
│           security/pat/   PatAuthenticationService, PatAuthenticationToken, PatPrincipal, PatSecurityCurrentUserProvider
├── agent/           registered machines
│   ├── api/                 ← NamedInterface "api": AgentAuthenticator, CurrentAgentProvider, controllers, DTOs
│   ├── application/AgentRegistrationService, AgentService, AgentConfigurationService (heartbeat)
│   ├── domain/model/Agent, AgentStatus, AuthType, SystemInfo (@Embeddable)
│   ├── domain/repository/AgentRepository (port)
│   └── infrastructure/
│       ├── repository/jpa/  JpaAgentRepository ("jpaAgentRepository") + AgentJpaRepository
│       ├── repository/inMemory/InMemoryAgentRepository  ❌ stub, every method returns null/empty
│       └── security/        AgentAuthenticationService, AgentAuthenticationToken, AgentPrincipal, CurrentAgentProviderImpl
├── security/        allowedDependencies = {"user::api", "common", "agent :: api"}
│   ├── api/AuthenticationController       POST /app/auth/login
│   ├── application/AuthenticationService, JwtService
│   └── infrastructure/
│       ├── chain/   4 SecurityFilterChain beans (see §3)
│       ├── filter/  JwtFilter, PatFilter, AgentFilter
│       └── configuration/ CorsConfig, JwtConfiguration (@ConfigurationProperties "security.jwt"), SecurityConfiguration (BCrypt, AuthenticationManager)
└── telemetry/       metrics ingest + dashboard read
    ├── api/controller/AgentTelemetryController, TelemetryDashboardController
    ├── api/request/TelemetryBatchRequest, MetricRequest ; api/respsonse/TelemetryBatchResponse  (folder typo "respsonse")
    ├── application/AgentTelemetryService, TelemetryDashboardService
    ├── domain/model/TelemetryBatch, Metric (JPA entities), MetricUnit
    ├── domain/repository/LatestMetricsRepository (port), TelemetryBatchRepository + MetricRepository (Spring Data, UNUSED)
    └── infrastructure/repository/InMemoryLatestMetricsRepository  ConcurrentHashMap<agentId, TelemetryBatch>
```

Pattern used for repositories: a domain **port** interface (e.g. `AgentRepository`) with one or
more adapters, picked with `@Qualifier("jpaAgentRepository")`. Same for `CurrentUserProvider`:
`userSecurityCurrentUserProvider` (JWT context) vs `patSecurityCurrentUserProvider` (PAT context) —
**use the one that matches the filter chain the endpoint runs under**, otherwise you get a
`ClassCastException` on the principal.

⚠ Modulith boundary: `telemetry/application/TelemetryDashboardService` (uncommitted change)
imports `agent.domain.model.Agent` and `agent.domain.repository.AgentRepository`, which are **not**
in agent's `api` named interface. The `verifyModularStructure` test will probably flag this.
Fix by exposing a lookup method (e.g. `Long internalIdFor(String publicId)`) on something in
`agent.api`.

---

## 3. Security: four filter chains

Requests are matched against chains in `@Order`. The first chain whose `securityMatcher` matches
handles the request exclusively.

| Order | Bean / class | Matches | Filter | Principal | Authenticates with |
|---|---|---|---|---|---|
| 1 | `PublicSecurityFilterChain` | `/app/auth/**`, `/app/user/register-user/**` | none | — | permitAll |
| 2 | `AgentFilterChain` | `/app/agent/telemetry/**`, `/app/agent/heartbeat/**` | `AgentFilter` | `AgentPrincipal` | `agt_…` → SHA-256 → `Agent.secureHashToken` |
| 3 | `PersonalAgentTokenSecurityChain` | `/app/agent/**` (in practice `register-agent`) | `PatFilter` | `PatPrincipal` | `pat_…` → SHA-256 → `PersonalAccessToken.tokenHash`, not revoked/expired |
| 4 | `JwtSecurityFilterChain` | `/app/user/pat/**`, `/app/user/me/`, `/app/telemetry/dashboard/**`, `/app/dashboard/agent/**` | `JwtFilter` | `CurrentUser` | JWT signature + subject(email) + expiry |

Order 2 must come before 3 because `/app/agent/**` would otherwise swallow telemetry/heartbeat.
Any path not matched by a chain falls through to Spring Boot's default security (all
authenticated, form login).

All chains are stateless, CSRF disabled, CORS via the shared `CorsConfigurationSource` bean
(allows `http://localhost:*` and `http://127.0.0.1:*`, credentials on), and permit `OPTIONS`.

Filters skip (pass through without authenticating) when the header is missing or has the wrong
prefix → the request then fails `authenticated()` with 401/403.

---

## 4. Endpoints → code

| Method & path | Auth | Controller → Service | What it does |
|---|---|---|---|
| `POST /app/user/register-user/` | public | `UserController.registerUser` → `UserService.registerUser` | Validates DTO (`@Valid`, password regex), 409 if email exists, BCrypt, role `ROLE_USER`, publicId `user_…` |
| `POST /app/auth/login` | public | `AuthenticationController` → `AuthenticationService.login` | `AuthenticationManager` (DaoAuthenticationProvider + `UserDetailsServiceImpl`) → JWT with `roles` claim |
| `GET /app/user/me/` | JWT | `UserController.getCurrentUser` | User + list of their PATs (`personalAccessTokenResponseDtoList`) |
| `POST /app/user/pat/register-pat/` | JWT | `PatController` → `PersonalAccessTokenService.createToken` | `pat_` + random key; stores SHA-256; returns **raw** token string once |
| `PUT /app/user/pat/revoke-pat/?tokenId=` | JWT | `PersonalAccessTokenService.revokePat` | Sets `revoked=true` (no ownership check) |
| `POST /app/agent/register-agent/` | PAT | `AgentRegistrationController` → `AgentRegistrationService.registerAgent` | 409 if `installationId` exists; status OFFLINE; token `agt_…`, stores hash; returns `{agentId, secureKey, installationId}` **201** |
| `PUT /app/agent/heartbeat/` | Agent | `AgentHeartbeatController` → `AgentConfigurationService.agentHeartbeatCheck` | `status=ONLINE`, `lastHeartBeat=now`, saved to DB |
| `POST /app/agent/telemetry/submit/` | Agent | `AgentTelemetryController` → `AgentTelemetryService.submitTelemetry` | Maps request → `TelemetryBatch`, sets `agentId` (internal Long), **puts in RAM map** (overwrites previous) |
| `GET /app/telemetry/dashboard/{publicId}` | JWT | `TelemetryDashboardController` → `TelemetryDashboardService.getBatch` | publicId → Agent → internal id → latest batch, 404 if none |
| `GET /app/dashboard/agent/all-agents/` | JWT | `AgentController` → `AgentService.getAllAgents` | Agents where `userId = current user` |
| `GET /app/dashboard/agent/{publicId}` | JWT | `AgentService.findByPublicId` | One agent (no ownership check) |
| `DELETE /app/dashboard/agent/{publicId}` | JWT | `AgentService.deleteAgent` | `deleteByPublicId` (no ownership check, no `@Transactional`) |
| `DELETE /app/dashboard/agent/all-agents/` | JWT | `AgentService.deleteAllAgents` | ⚠ `repository.deleteAll()` — **every user's** agents |

Error handling: `GlobalExceptionHandler` maps
`ResourceNotFoundException→404`, `ResourceAlreadyExistsException→409`,
`TokenInvalidException/UnauthenticatedException→401`,
`AgentCredentialGenerationException→500`, anything else → **500 with the exception message**.
Note: exceptions thrown inside *filters* (e.g. bad agent token → `AgentExceptionError`, expired
JWT → `ExpiredJwtException`) happen before the DispatcherServlet, so the handler does **not**
catch them — the client gets a raw Spring error response.

Validation errors (`MethodArgumentNotValidException`) also fall into the generic 500 handler —
a bad password at registration returns 500 instead of 400.

---

## 5. Data model (MySQL tables, created by Hibernate)

```
user                      user_roles            personal_access_token
─────────                 ──────────            ─────────────────────
id (PK, long)             user_id → user.id     id (PK)
public_id  "user_…"       role (ROLE_USER…)     public_id (22 hex chars)
first_name, last_name                           token_name
email                                           token_hash (SHA-256 hex)
password_hash (BCrypt)                          creation_date, expiration_date_and_time
account_status (ordinal)                        revoked
registered_at                                   user_id → user.id

agent                                           (telemetry_batch, metrics, metric_attributes
─────                                            tables are created from the entities but
id (PK)                                          NOTHING writes to them yet)
public_id "ag_" + 20 hex
device_name, auth_type, version
status (ordinal!), secure_hash_token
installation_id, user_id (plain long, no FK)
java_version, operating_system, architecture   ← @Embedded SystemInfo
registered_at, last_seen_at (never set), last_heart_beat
```

Enums `AgentStatus`, `AuthType`, `AccountStatus` are stored **as ordinals** (no `@Enumerated(STRING)`)
— reordering those enums will corrupt existing rows.

`Agent.userId` is a raw `long` rather than a relation, on purpose (cross-module boundary).

---

## 6. Telemetry ingest — current behaviour and its main limitation

The agent sends one batch per collector:
`{ collectorId: "cpu", timestamp, metrics: [ {name, value, unit, timestamp, attributes} ] }`

On the server:
- `TelemetryBatchRequest` has no `collectorId` field → the batch-level collector id is **dropped**.
- `MetricRequest.collectorId` exists but the agent never sends it → `null`.
- `InMemoryLatestMetricsRepository.save(agentId, batch)` **replaces** the previous batch for that agent.

Result: `GET /app/telemetry/dashboard/{id}` returns whichever collector reported last
(cpu *or* memory *or* disk), and `collectorId` in the response is always `null`.

Options to fix (pick one):
1. Add `collectorId` to `TelemetryBatchRequest` + `TelemetryBatch`, and change the in-memory store to
   `Map<Long, Map<String, TelemetryBatch>>`; the dashboard endpoint merges all collectors.
2. Or persist every batch via `TelemetryBatchRepository` (gives history for free) and query the
   newest per collector.

---

## 7. Known bugs & gaps (with locations)

Paths relative to `src/main/java/com/telemtry/telemetryserver/`.

| # | Where | Problem |
|---|---|---|
| 1 | `user/application/pat/PersonalAccessTokenService.java:101` | `getExpirationDateAndTime().isBefore(...)` NPE when PAT created without `expirationDate` → agent registration 500 |
| 2 | `agent/application/AgentService.java:60` | `deleteAllAgents` deletes all agents in the DB, not just the user's |
| 3 | `agent/application/AgentService.java:89` | `deleteAgent` → Spring Data derived delete without `@Transactional` (likely `TransactionRequiredException`; not verified at runtime) |
| 4 | `AgentService.findByPublicId`, `deleteAgent`, `TelemetryDashboardService.getBatch`, `revokePat` | No check that the resource belongs to the current user |
| 5 | `agent/api/response/AgentResponseDto` | Only `agentId, secureKey, installationId` — dashboard can't show `deviceName`, `status`, `lastHeartBeat`, OS |
| 6 | — | No job marks agents OFFLINE; once ONLINE, always ONLINE |
| 7 | `security/infrastructure/filter/JwtFilter.java:98` | "JWT validation failed" is logged even on success (missing `else`) |
| 8 | `JwtFilter` | Expired/malformed JWT throws from `extractUsername` inside the filter → unhandled error instead of clean 401 |
| 9 | `telemetry/application/AgentTelemetryService.java:42` | Telemetry only in RAM; `System.out.println(batchRequest)` on every request |
| 10 | `agent/infrastructure/repository/inMemory/InMemoryAgentRepository` | Stub; returns `null` from `save` — don't inject it |
| 11 | `AgentRegistrationService.heartbeatCheck()` | Empty leftover method |
| 12 | `common/exception/GlobalExceptionHandler` | No handler for validation errors (→500), leaks exception messages on 500, `printStackTrace` everywhere |
| 13 | `src/main/resources/application.properties.example:11` | `c:mysql://` should be `jdbc:mysql://` |
| 14 | `Dockerfile` | Two `FROM` lines, empty `RUN`, entrypoint `top -b` — placeholder |
| 15 | `pom.xml` | Unneeded Kotlin plugin & deps; both MySQL and MariaDB drivers |
| 16 | `AgentRequestDto.authType` | Enum is `API_KEY, PAIRING_CODE`; `API.md` example sends `"PAT"` which would fail to deserialize. Agent currently sends nothing |
| 17 | `security/infrastructure/chain/*` | Swagger / actuator paths are not in any custom chain |

---

## 8. Where to add things

- **New dashboard endpoint (JWT):** controller in the owning module's `api/controller`, then add
  its path to `JwtSecurityFilterChain.securityMatcher(...)` — otherwise it falls to default
  security. Use `@Qualifier("userSecurityCurrentUserProvider")`.
- **New agent-facing endpoint:** put it under `/app/agent/telemetry/**` or `/app/agent/heartbeat/**`
  or extend `AgentFilterChain`'s matcher; use `CurrentAgentProvider` to get the calling agent.
- **Telemetry history:** start using `TelemetryBatchRepository` in `AgentTelemetryService` (batch
  has `cascade = ALL` to metrics, and `Metric.telemetryBatch` back-reference is already set in
  `mapToTelemetryBatch`). Add an index on `(agent_id, timestamp)`.
- **Offline detection:** `@EnableScheduling` + a `@Scheduled(fixedDelay=…)` method in
  `agent/application` that marks agents with old `lastHeartBeat` as `OFFLINE`.
