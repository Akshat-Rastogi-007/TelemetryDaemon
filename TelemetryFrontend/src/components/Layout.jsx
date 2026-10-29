import { Link, Outlet, useNavigate } from 'react-router-dom'
import { session } from '../api'

export default function Layout() {
  const navigate = useNavigate()
  const logout = () => {
    session.end()
    navigate('/auth')
  }

  return (
    <div className="shell">
      <header className="topbar">
        <Link to="/" className="brand">
          <span className="brand-dot" /> telemetry
        </Link>
        <div className="topbar-right">
          {session.isDemo() && <span className="pill pill-warn">demo mode</span>}
          <button className="btn btn-ghost" onClick={logout}>Log out</button>
        </div>
      </header>
      <main className="content">
        <Outlet />
      </main>
    </div>
  )
}
