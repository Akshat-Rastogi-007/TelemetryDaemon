import { useEffect, useRef, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { api } from '../api'
import Sparkline from '../components/Sparkline'
import Terminal from '../components/Terminal'

const POLL_MS = 3000
const WINDOW = 40

const BYTE_UNITS = { BYTE: 1, KILOBYTE: 1024, MEGABYTE: 1024 ** 2, GIGABYTE: 1024 ** 3 }

function formatBytes(n) {
  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  let i = 0
  while (n >= 1024 && i < units.length - 1) { n /= 1024; i++ }
  return `${n.toFixed(n < 10 && i ? 1 : 0)} ${units[i]}`
}

function formatMetric(m) {
  if (m.unit === 'PERCENT') return `${m.value.toFixed(1)}%`
  if (m.unit in BYTE_UNITS) return formatBytes(m.value * BYTE_UNITS[m.unit])
  if (m.name.includes('memory') || m.name.startsWith('disk')) return formatBytes(m.value)
  return Number.isInteger(m.value) ? String(m.value) : m.value.toFixed(2)
}

const get = (metrics, name) => metrics.find((m) => m.name === name)?.value

export default function AgentView() {
  const { id } = useParams()
  const [agent, setAgent] = useState(null)
  const [batch, setBatch] = useState(null)
  const [error, setError] = useState('')
  const [history, setHistory] = useState({ cpu: [], mem: [] })
  const [stream, setStream] = useState([])
  const [paused, setPaused] = useState(false)
  const pausedRef = useRef(paused)
  pausedRef.current = paused

  useEffect(() => {
    api.agent(id).then(setAgent).catch(() => {})
  }, [id])

  useEffect(() => {
    let alive = true
    const tick = async () => {
      if (pausedRef.current) return
      try {
        const b = await api.telemetry(id)
        if (!alive || !b) return
        setError('')
        setBatch(b)
        const metrics = b.metrics ?? []
        const cpu = get(metrics, 'cpu.usage')
        const mem = get(metrics, 'used.memory.percentage')
        setHistory((h) => ({
          cpu: cpu == null ? h.cpu : [...h.cpu, cpu].slice(-WINDOW),
          mem: mem == null ? h.mem : [...h.mem, mem].slice(-WINDOW),
        }))
        setStream((s) => [{ at: b.timestamp ?? new Date().toISOString(), metrics }, ...s].slice(0, 50))
      } catch (e) {
        if (alive) setError(e.message)
      }
    }
    tick()
    const t = setInterval(tick, POLL_MS)
    return () => { alive = false; clearInterval(t) }
  }, [id])

  const metrics = batch?.metrics ?? []
  const cpu = get(metrics, 'cpu.usage')
  const mem = get(metrics, 'used.memory.percentage')
  const diskTotal = get(metrics, 'disk.total')
  const diskUsed = get(metrics, 'disk.used')
  const diskPct = diskTotal ? (diskUsed / diskTotal) * 100 : null
  const hostname = agent?.deviceName ?? id
  const online = (agent?.status ?? 'ONLINE') !== 'OFFLINE' && !error

  return (
    <>
      <Link to="/" className="back">← All agents</Link>
      <section className="page-head row-between">
        <div>
          <h1 className="agent-title">
            <span className={`status status-${online ? 'online' : 'offline'}`} /> {hostname}
          </h1>
          <div className="mono small muted">{id}</div>
        </div>
        <div className="head-meta">
          <span className="small muted">
            {batch?.timestamp ? `Last batch ${new Date(batch.timestamp).toLocaleTimeString()}` : 'Waiting for telemetry…'}
          </span>
          <button className="btn btn-ghost btn-sm" onClick={() => setPaused((p) => !p)}>{paused ? 'Resume' : 'Pause'}</button>
        </div>
      </section>

      {error && <div className="alert alert-err">{error}. Is the agent running and sending telemetry?</div>}

      <section className="kpis">
        <div className="card kpi">
          <div className="kpi-label">CPU</div>
          <div className="kpi-value">{cpu == null ? '—' : `${cpu.toFixed(1)}%`}</div>
          <Sparkline values={history.cpu} />
          <div className="small muted">load {get(metrics, 'load.average')?.toFixed(2) ?? '—'} · {get(metrics, 'logical.processors') ?? '—'} cores</div>
        </div>
        <div className="card kpi">
          <div className="kpi-label">Memory</div>
          <div className="kpi-value">{mem == null ? '—' : `${mem.toFixed(1)}%`}</div>
          <Sparkline values={history.mem} />
          <div className="small muted">
            {get(metrics, 'used.memory') != null ? `${formatBytes(get(metrics, 'used.memory'))} of ${formatBytes(get(metrics, 'total.memory'))}` : '—'}
          </div>
        </div>
        <div className="card kpi">
          <div className="kpi-label">Disk</div>
          <div className="kpi-value">{diskPct == null ? '—' : `${diskPct.toFixed(1)}%`}</div>
          <div className="meter"><div className="meter-fill" style={{ width: `${diskPct ?? 0}%` }} /></div>
          <div className="small muted">{diskTotal ? `${formatBytes(diskUsed)} of ${formatBytes(diskTotal)} used` : '—'}</div>
        </div>
      </section>

      <section className="split">
        <div className="card panel">
          <div className="section-head">
            <h2>Incoming telemetry</h2>
            <span className="muted small">every {POLL_MS / 1000}s{paused ? ' · paused' : ''}</span>
          </div>
          <div className="stream">
            {stream.length === 0 && <p className="muted small">No batches yet.</p>}
            {stream.map((s, i) => (
              <div key={s.at + i} className={`stream-row ${i === 0 ? 'stream-new' : ''}`}>
                <span className="mono small muted">{new Date(s.at).toLocaleTimeString()}</span>
                <div className="stream-metrics">
                  {s.metrics.map((m, j) => (
                    <span key={j} className="chip"><b>{m.name}</b> {formatMetric(m)}</span>
                  ))}
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="panel-term">
          <h2>Terminal</h2>
          <Terminal hostname={hostname} online={online} />
        </div>
      </section>

      {metrics.length > 0 && (
        <section className="card panel">
          <h2>Latest metrics</h2>
          <div className="table-wrap">
            <table>
              <thead><tr><th>Metric</th><th>Value</th><th>Unit</th><th>Collector</th></tr></thead>
              <tbody>
                {metrics.map((m, i) => (
                  <tr key={i}>
                    <td className="mono">{m.name}</td>
                    <td>{formatMetric(m)}</td>
                    <td className="muted">{m.unit}</td>
                    <td className="muted">{m.collectorId ?? '—'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      )}
    </>
  )
}
