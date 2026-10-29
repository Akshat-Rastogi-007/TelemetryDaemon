import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import { session } from './api'
import Auth from './pages/Auth'
import Home from './pages/Home'
import AgentView from './pages/AgentView'
import Layout from './components/Layout'
import './styles.css'

function Protected({ children }) {
  return session.token() ? children : <Navigate to="/auth" replace />
}

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/auth" element={<Auth />} />
        <Route element={<Protected><Layout /></Protected>}>
          <Route path="/" element={<Home />} />
          <Route path="/agents/:id" element={<AgentView />} />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>,
)
