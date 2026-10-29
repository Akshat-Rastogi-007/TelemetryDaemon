import { useState } from 'react'

export default function CopyButton({ text }) {
  const [done, setDone] = useState(false)
  const copy = async () => {
    try {
      await navigator.clipboard.writeText(text)
      setDone(true)
      setTimeout(() => setDone(false), 1200)
    } catch { /* clipboard unavailable */ }
  }
  return (
    <button type="button" className="btn btn-ghost btn-xs" onClick={copy}>
      {done ? 'Copied' : 'Copy'}
    </button>
  )
}
