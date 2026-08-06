# REST API Documentation

## Authentication

Telemetry uses three authentication mechanisms.

| Type | Used By |
|------|----------|
| JWT | Dashboard Users |
| Personal Access Token | Agent Registration |
| Agent Token | Telemetry Agent |

---

# Public APIs

## Register User

```
POST /app/user/register-user/
```

---

## Login

```
POST /app/auth/login
```

Returns JWT.

---

# Agent Registration

Authentication Required:

```
Authorization: Bearer <PAT>
```

---

## Register Agent

```
POST /app/agent/register-agent/
```

Registers a new telemetry agent.

---

# Telemetry APIs

Authentication Required

```
Authorization: Bearer <Agent Token>
```

---

## Submit Telemetry

```
POST /app/agent/telemetry/submit/
```

Uploads telemetry from an authenticated agent.

---

# Dashboard APIs

Authentication Required

```
Authorization: Bearer <JWT>
```

---

## Agents

```
GET /app/agent/dashboard/all-agents/
```

Returns all registered agents.

---

```
DELETE /app/agent/dashboard/all-agents/
```

Deletes all agents.

---

## Telemetry

```
GET /app/telemetry/dashboard/{agentId}
```

Returns the latest telemetry batch.

---

# Response Codes

| Code | Meaning |
|------|----------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 409 | Conflict |
| 500 | Internal Server Error |