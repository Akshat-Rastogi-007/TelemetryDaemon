import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api, session } from '../api'

const PASSWORD_RULE = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,64}$/

export default function Auth() {
  const navigate = useNavigate()
  const [mode, setMode] = useState('login')
  const [form, setForm] = useState({ firstName: '', lastName: '', email: '', password: '' })
  const [error, setError] = useState('')
  const [info, setInfo] = useState('')
  const [busy, setBusy] = useState(false)

  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value })

  async function submit(e) {
    e.preventDefault()
    setError('')
    setInfo('')
    setBusy(true)
    try {
      if (mode === 'register') {
        if (!PASSWORD_RULE.test(form.password)) {
          throw new Error('Password needs 8–64 chars with upper, lower, digit and special character.')
        }
        await api.register(form)
        setInfo('Account created. Log in to continue.')
        setMode('login')
      } else {
        const res = await api.login(form.email, form.password)
        session.start(res.token)
        navigate('/')
      }
    } catch (err) {
      setError(err.message)
    } finally {
      setBusy(false)
    }
  }

  function demo() {
    session.start('demo-token', true)
    navigate('/')
  }

  return (
    <div className="auth-wrap">
      <div className="auth-side">
        <div className="brand brand-lg"><span className="brand-dot" /> telemetry</div>
        <p className="auth-tag">Self-hosted observability for your machines. Create a token, log in your agent and watch its metrics live.</p>
        <ol className="auth-steps">
          <li>Create your account</li>
          <li>Generate a Personal Access Token</li>
          <li>Log in your agent with the token</li>
          <li>Watch telemetry stream in</li>
        </ol>
      </div>

      <form className="card auth-card" onSubmit={submit}>
        <div className="tabs">
          <button type="button" className={mode === 'login' ? 'tab active' : 'tab'} onClick={() => setMode('login')}>Log in</button>
          <button type="button" className={mode === 'register' ? 'tab active' : 'tab'} onClick={() => setMode('register')}>Create account</button>
        </div>

        {mode === 'register' && (
          <div className="row">
            <label>First name<input required value={form.firstName} onChange={set('firstName')} /></label>
            <label>Last name<input required value={form.lastName} onChange={set('lastName')} /></label>
          </div>
        )}
        <label>Email<input type="email" required value={form.email} onChange={set('email')} autoComplete="email" /></label>
        <label>Password<input type="password" required value={form.password} onChange={set('password')} autoComplete={mode === 'login' ? 'current-password' : 'new-password'} /></label>

        {error && <div className="alert alert-err">{error}</div>}
        {info && <div className="alert alert-ok">{info}</div>}

        <button className="btn btn-primary" disabled={busy}>
          {busy ? 'Please wait…' : mode === 'login' ? 'Log in' : 'Create account'}
        </button>
        <button type="button" className="btn btn-ghost" onClick={demo}>Explore with demo data</button>
      </form>
    </div>
  )
}
