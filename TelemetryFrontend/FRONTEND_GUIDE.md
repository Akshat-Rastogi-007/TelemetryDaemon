# Telemetry Frontend — Developer Guide

> Snapshot: 2026-10-29. Back to the overview: [`../PROJECT_STATUS.md`](../PROJECT_STATUS.md)
> Quick-start for users: [`README.md`](README.md)

⚠ **This whole folder is untracked in git** as of the snapshot — commit it
(`node_modules/` is already in its `.gitignore`).

React 18 + Vite 5 + react-router-dom 6. No UI kit, no state library, no TypeScript, no tests —
just `fetch`, `useState`/`useEffect`, and one hand-written `styles.css`.

---

## 1. Run

```bash
cd TelemetryFrontend
npm install
npm run dev            # http://localhost:3000 (strictPort — fails if 3000 is taken)
npm run build          # → dist/
```

Server URL: `VITE_API_URL` (default `http://localhost:8080`). Put overrides in `.env.local`.

CORS: the server now allows any `http://localhost:*` origin (uncommitted `CorsConfig` change), so
port 3000 is no longer strictly required, but `vite.config.js` still pins it.

**Demo mode:** "Explore with demo data" on the login page stores a fake token + `demo=1` in
localStorage; every `api.*` call is then answered locally with fake agents and random-walk metrics.
Useful for UI work without the server/agent running.

---

## 2. File map

```
src/
├── main.jsx            Router: /auth (public), / and /agents/:id (behind <Protected>, wrapped in <Layout>)
├── api.js              ★ the only place that talks to the server
│                         - session: jwt + demo flag in localStorage (try/catch wrapped)
│                         - request(): adds Bearer JWT, unwraps ApiResponseDto → .data, throws json.message on !ok
│                         - demo data generators
│                         - api.{login, register, me, createPat, agents, agent, deleteAgent, telemetry}
├── pages/
│   ├── Auth.jsx        Login / Create-account tabs; client-side password regex; demo button
│   ├── Home.jsx        Greeting (api.me) · Step 1: PAT form (name + expiry date) → shows token once w/ copy
│   │                   Step 2: copyable agent login/start commands · agent grid (polls every 5s), Open / Remove
│   └── AgentView.jsx   Polls api.telemetry(id) every 3s: CPU / Memory / Disk KPI cards, sparklines (last 40),
│                       "Incoming telemetry" stream (last 50 batches), Pause button, simulated Terminal, metrics table
├── components/
│   ├── Layout.jsx      Top bar, demo-mode pill, Log out (clears session)
│   ├── Sparkline.jsx   Tiny SVG line+area chart
│   ├── Terminal.jsx    Fake shell (help, ps, df, free, ping…) — no backend
│   └── CopyButton.jsx
└── styles.css
```

---

## 3. API usage (server contract)

| `api.*` | HTTP | Uses fields |
|---|---|---|
| `login(email, pw)` | `POST /app/auth/login` | `data.token` → localStorage `jwt` |
| `register(user)` | `POST /app/user/register-user/` | — |
| `me()` | `GET /app/user/me/` | `firstName` |
| `createPat(name, "YYYY-MM-DDT00:00:00")` | `POST /app/user/pat/register-pat/` | raw token string |
| `agents()` | `GET /app/dashboard/agent/all-agents/` | `agentId`, `deviceName`, `status`, `os`, `arch` |
| `agent(id)` | `GET /app/dashboard/agent/{id}` | `deviceName`, `status` |
| `deleteAgent(id)` | `DELETE /app/dashboard/agent/{id}` | — |
| `telemetry(id)` | `GET /app/telemetry/dashboard/{id}` | `timestamp`, `metrics[{name,value,unit,collectorId}]` |

The frontend always sends an `expirationDate` for PATs, so it avoids the server's null-expiry NPE.

---

## 4. Gaps between UI and the real server

These are why the real (non-demo) dashboard looks thinner than demo mode:

1. **Agent cards show the id, not a name, and always "online".** The server's `AgentResponseDto`
   only returns `agentId, secureKey, installationId`. `deviceName`, `status`, `os`, `arch` are
   undefined → UI falls back to the id and to `ONLINE`. Fix server-side (and make the agent send
   `deviceName`).
2. **KPI cards flicker / show "—".** The server returns only the latest batch, which is *one*
   collector (cpu or memory or disk). So on each 3s poll only one of the three cards has data.
   `AgentView` keeps sparkline history but the KPI values reset to "—" for the other two.
   Fix server-side (see `SERVER_GUIDE.md` §6) or merge per-collector in `AgentView`.
3. **"Collector" column is always "—"** — server never fills `collectorId`.
4. **Disk card** reads the first `disk.total`/`disk.used` it finds — with many Linux file stores that
   may be a tmpfs, not `/`.
5. **Memory %** takes the first `used.memory.percentage`; the agent sends that name twice (2nd is
   a buggy swap value). First one is correct, so this works by luck.
6. **Terminal is fake.** No command channel exists between server and agent.
7. **Expired JWT** isn't handled specially — requests just start failing with an error message; the
   user has to click Log out manually. No redirect to `/auth` on 401.
8. **Remove agent** swallows errors (`.catch(() => {})`) and removes the card locally even if the
   server delete failed.

---

## 5. Ideas / next steps

- Redirect to `/auth` on 401 in `request()`.
- Merge per-collector batches client-side as a stop-gap (`Map<collectorId, batch>` in `AgentView`).
- Show agent `lastHeartBeat` / registered date once the server DTO has them.
- PAT management list (from `me().personalAccessTokenResponseDtoList`) with revoke buttons
  (`PUT /app/user/pat/revoke-pat/?tokenId=`).
- History charts once the server persists telemetry.
