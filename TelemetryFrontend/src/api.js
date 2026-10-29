// Thin client for the Telemetry Server REST API (see API.md).
// When demo mode is on, every call is answered locally with fake data so the
// UI can be shown without a running server.

const BASE_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

const store = {
  get: (k) => { try { return localStorage.getItem(k) } catch { return null } },
  set: (k, v) => { try { localStorage.setItem(k, v) } catch { /* ignore */ } },
  del: (k) => { try { localStorage.removeItem(k) } catch { /* ignore */ } },
}

export const session = {
  token: () => store.get('jwt'),
  isDemo: () => store.get('demo') === '1',
  start(token, demo = false) {
    store.set('jwt', token)
    demo ? store.set('demo', '1') : store.del('demo')
  },
  end() {
    store.del('jwt')
    store.del('demo')
  },
}

async function request(path, { method = 'GET', body } = {}) {
  const headers = { 'Content-Type': 'application/json' }
  const token = session.token()
  if (token) headers.Authorization = `Bearer ${token}`

  let res
  try {
    res = await fetch(BASE_URL + path, {
      method,
      headers,
      body: body ? JSON.stringify(body) : undefined,
    })
  } catch {
    throw new Error(`Cannot reach server at ${BASE_URL}`)
  }

  const json = await res.json().catch(() => null)
  if (!res.ok) throw new Error(json?.message || `Request failed (${res.status})`)
  // Server wraps payloads in ApiResponseDto { data, statusCode, message }
  return json && 'data' in json ? json.data : json
}

/* ---------------------------------------------------------------- demo data */

const delay = (v, ms = 250) => new Promise((r) => setTimeout(() => r(v), ms))

const demoAgents = [
  { agentId: 'ag_7f3a91c0d2e84b6a1f90', deviceName: 'akshat-laptop', installationId: 'inst-1', status: 'ONLINE', os: 'Linux', arch: 'amd64' },
  { agentId: 'ag_19be04f7a3c25d8e6b11', deviceName: 'build-server-01', installationId: 'inst-2', status: 'ONLINE', os: 'Linux', arch: 'aarch64' },
  { agentId: 'ag_c04d7e2b9a1f63850e72', deviceName: 'office-desktop', installationId: 'inst-3', status: 'OFFLINE', os: 'Windows 11', arch: 'amd64' },
]

const walkState = {}
function walk(key, start, min, max, step) {
  const prev = walkState[key] ?? start
  const next = Math.min(max, Math.max(min, prev + (Math.random() - 0.5) * step))
  walkState[key] = next
  return next
}

function demoBatch(agentId) {
  const now = new Date().toISOString()
  const GB = 1024 ** 3
  const totalMem = 16 * GB
  const memPct = walk(agentId + 'mem', 55, 20, 92, 6)
  const totalDisk = 512 * GB
  const diskUsed = walk(agentId + 'disk', 0.62, 0.6, 0.64, 0.002) * totalDisk
  const m = (name, value, unit, collectorId) => ({ name, value, unit, timestamp: now, collectorId, attributes: {} })
  return {
    collectorId: 'all',
    timestamp: now,
    metrics: [
      m('cpu.usage', walk(agentId + 'cpu', 30, 2, 98, 18), 'PERCENT', 'cpu'),
      m('logical.processors', 8, 'COUNT', 'cpu'),
      m('load.average', walk(agentId + 'load', 1.4, 0.1, 6, 0.6), 'COUNT', 'cpu'),
      m('total.memory', totalMem, 'BYTE', 'memory'),
      m('used.memory', (memPct / 100) * totalMem, 'BYTE', 'memory'),
      m('free.memory', (1 - memPct / 100) * totalMem, 'BYTE', 'memory'),
      m('used.memory.percentage', memPct, 'PERCENT', 'memory'),
      m('disk.total', totalDisk, 'BYTE', 'disk'),
      m('disk.used', diskUsed, 'BYTE', 'disk'),
      m('disk.free', totalDisk - diskUsed, 'BYTE', 'disk'),
    ],
  }
}

/* ------------------------------------------------------------------ the API */

export const api = {
  login: (email, password) => request('/app/auth/login', { method: 'POST', body: { email, password } }),

  register: (user) => request('/app/user/register-user/', { method: 'POST', body: user }),

  me: () =>
    session.isDemo()
      ? delay({ firstName: 'Demo', lastName: 'User', email: 'demo@telemetry.dev', personalAccessTokenResponseDtoList: [] })
      : request('/app/user/me/'),

  createPat: (tokenName, expirationDate) =>
    session.isDemo()
      ? delay('pat_' + crypto.randomUUID().replace(/-/g, ''))
      : request('/app/user/pat/register-pat/', { method: 'POST', body: { tokenName, expirationDate } }),

  agents: () => (session.isDemo() ? delay(demoAgents) : request('/app/dashboard/agent/all-agents/')),

  agent: (id) =>
    session.isDemo()
      ? delay(demoAgents.find((a) => a.agentId === id))
      : request(`/app/dashboard/agent/${encodeURIComponent(id)}`),

  deleteAgent: (id) =>
    session.isDemo() ? delay(null) : request(`/app/dashboard/agent/${encodeURIComponent(id)}`, { method: 'DELETE' }),

  telemetry: (id) =>
    session.isDemo() ? delay(demoBatch(id), 120) : request(`/app/telemetry/dashboard/${encodeURIComponent(id)}`),
}
