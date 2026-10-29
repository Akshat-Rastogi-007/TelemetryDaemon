import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api'
import CopyButton from '../components/CopyButton'

const AGENT_JAR = 'java -jar target/TelemetryAgent-1.0-SNAPSHOT.jar'

function defaultExpiry() {
  const d = new Date()
  d.setMonth(d.getMonth() + 3)
  return d.toISOString().slice(0, 10)
}

export default function Home() {
  const [user, setUser] = useState(null)
  const [agents, setAgents] = useState([])
  const [agentsError, setAgentsError] = useState('')
  const [pat, setPat] = useState('')
  const [patForm, setPatForm] = useState({ name: 'agent-registration', expiry: defaultExpiry() })
  const [patError, setPatError] = useState('')
  const [busy, setBusy] = useState(false)

  useEffect(() => {
    api.me().then(setUser).catch(() => {})
  }, [])

  // Poll so a freshly logged-in agent appears without a refresh
  useEffect(() => {
    let alive = true
    const load = () =>
      api.agents()
        .then((list) => { if (alive) { setAgents(list ?? []); setAgentsError('') } })
        .catch((e) => alive && setAgentsError(e.message))
    load()
    const t = setInterval(load, 5000)
    return () => { alive = false; clearInterval(t) }
  }, [])

  async function createPat(e) {
    e.preventDefault()
    setPatError('')
    setBusy(true)
    try {
      setPat(await api.createPat(patForm.name, `${patForm.expiry}T00:00:00`))
    } catch (err) {
      setPatError(err.message)
    } finally {
      setBusy(false)
    }
  }

  async function removeAgent(id) {
    await api.deleteAgent(id).catch(() => {})
    setAgents((a) => a.filter((x) => x.agentId !== id))
  }

  return (
    <>
      <section className="page-head">
        <h1>Hi{user?.firstName ? `, ${user.firstName}` : ''}</h1>
        <p className="muted">Connect a machine in two steps, then open it to see live telemetry and a terminal.</p>
      </section>

      <section className="steps">
        <div className="card step">
          <div className="step-num">1</div>
          <h2>Create a Personal Access Token</h2>
          <p className="muted">The token is only used once, to register an agent. It is shown a single time.</p>
          <form onSubmit={createPat} className="stack">
            <div className="row">
              <label>Token name<input required value={patForm.name} onChange={(e) => setPatForm({ ...patForm, name: e.target.value })} /></label>
              <label>Expires<input type="date" required value={patForm.expiry} onChange={(e) => setPatForm({ ...patForm, expiry: e.target.value })} /></label>
            </div>
            <button className="btn btn-primary" disabled={busy}>{busy ? 'Creating…' : 'Generate token'}</button>
          </form>
          {patError && <div className="alert alert-err">{patError}</div>}
          {pat && (
            <div className="secret">
              <div className="secret-label">Your token. Copy it now, you won't see it again.</div>
              <div className="code-line">
                <code>{pat}</code>
                <CopyButton text={pat} />
              </div>
            </div>
          )}
        </div>

        <div className={`card step ${pat ? '' : 'step-dim'}`}>
          <div className="step-num">2</div>
          <h2>Log in your agent</h2>
          <p className="muted">From the <code>TelemetryAgent</code> directory, run login and paste the token when prompted. Then start the agent.</p>
          <div className="code-block">
            <div className="code-line"><code><span className="prompt">$</span> {AGENT_JAR} login</code><CopyButton text={`${AGENT_JAR} login`} /></div>
            <div className="code-line"><code><span className="prompt">$</span> {AGENT_JAR} start</code><CopyButton text={`${AGENT_JAR} start`} /></div>
          </div>
          <p className="muted small">It shows up below within a few seconds.</p>
        </div>
      </section>

      <section>
        <div className="section-head">
          <h2>Your agents</h2>
          <span className="muted small">{agents.length} registered · refreshes every 5s</span>
        </div>
        {agentsError && <div className="alert alert-err">{agentsError}</div>}
        {agents.length === 0 && !agentsError ? (
          <div className="card empty">
            <div className="pulse" />
            <p>Waiting for your first agent…</p>
            <p className="muted small">Finish steps 1 and 2 above.</p>
          </div>
        ) : (
          <div className="agent-grid">
            {agents.map((a) => (
              <div key={a.agentId} className="card agent-card">
                <div className="agent-top">
                  <span className={`status status-${(a.status ?? 'ONLINE').toLowerCase()}`} />
                  <div className="agent-name">{a.deviceName ?? a.agentId}</div>
                </div>
                <div className="mono small muted">{a.agentId}</div>
                {(a.os || a.arch) && <div className="small muted">{[a.os, a.arch].filter(Boolean).join(' · ')}</div>}
                <div className="agent-actions">
                  <Link to={`/agents/${a.agentId}`} className="btn btn-primary btn-sm">Open</Link>
                  <button className="btn btn-ghost btn-sm" onClick={() => removeAgent(a.agentId)}>Remove</button>
                </div>
              </div>
            ))}
          </div>
        )}
      </section>
    </>
  )
}
