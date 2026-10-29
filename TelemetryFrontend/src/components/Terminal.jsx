import { useEffect, useRef, useState } from 'react'

// Simulated remote shell. There is no command-execution API on the server
// yet, so every command is answered locally from canned output.

function fakeOutput(cmd, args, ctx) {
  const now = new Date()
  switch (cmd) {
    case 'help':
      return [
        'Available commands:',
        '  help        show this help',
        '  whoami      current user on the agent',
        '  hostname    agent device name',
        '  uname       system information',
        '  uptime      time since boot and load',
        '  date        current date and time',
        '  ps          running processes',
        '  df          disk usage',
        '  free        memory usage',
        '  ls          list directory',
        '  echo TEXT   print text',
        '  ping HOST   ping a host',
        '  clear       clear the terminal',
      ].join('\n')
    case 'whoami': return 'telemetry'
    case 'hostname': return ctx.hostname
    case 'uname': return `Linux ${ctx.hostname} 6.11.0-generic #1 SMP x86_64 GNU/Linux`
    case 'date': return now.toString()
    case 'uptime': return ` ${now.toTimeString().slice(0, 8)} up 3 days,  4:12,  1 user,  load average: 1.42, 1.18, 0.97`
    case 'ps':
      return [
        '    PID TTY          TIME CMD',
        '      1 ?        00:00:04 systemd',
        '    812 ?        00:12:31 java -jar TelemetryAgent-1.0-SNAPSHOT.jar start',
        '   1044 ?        00:00:02 sshd',
        '   2210 pts/0    00:00:00 bash',
        '   2291 pts/0    00:00:00 ps',
      ].join('\n')
    case 'df':
      return [
        'Filesystem      Size  Used Avail Use% Mounted on',
        '/dev/nvme0n1p2  512G  318G  194G  63% /',
        '/dev/nvme0n1p1  1.0G  112M  912M  11% /boot',
        'tmpfs           7.8G  2.1M  7.8G   1% /tmp',
      ].join('\n')
    case 'free':
      return [
        '               total        used        free      shared  buff/cache   available',
        'Mem:            15Gi       8.6Gi       2.1Gi       412Mi       4.9Gi       6.4Gi',
        'Swap:          8.0Gi       256Mi       7.7Gi',
      ].join('\n')
    case 'ls': return 'config  logs  buffer  target  TelemetryAgent-1.0-SNAPSHOT.jar'
    case 'pwd': return '/opt/telemetry-agent'
    case 'echo': return args.join(' ')
    case 'ping': {
      const host = args[0] || 'localhost'
      return [`PING ${host} 56(84) bytes of data.`,
        ...[1, 2, 3].map((i) => `64 bytes from ${host}: icmp_seq=${i} ttl=57 time=${(8 + Math.random() * 12).toFixed(1)} ms`),
        `--- ${host} ping statistics ---`, '3 packets transmitted, 3 received, 0% packet loss'].join('\n')
    }
    case 'sudo': return 'sudo: remote privilege escalation is disabled for this agent'
    default: return `${cmd}: command not found`
  }
}

export default function Terminal({ hostname = 'agent', online = true }) {
  const [lines, setLines] = useState([
    { kind: 'sys', text: `Connected to ${hostname} (simulated session, commands are not executed yet)` },
    { kind: 'sys', text: "Type 'help' to see available commands." },
  ])
  const [input, setInput] = useState('')
  const [history, setHistory] = useState([])
  const [hIdx, setHIdx] = useState(-1)
  const [running, setRunning] = useState(false)
  const bodyRef = useRef(null)
  const inputRef = useRef(null)

  useEffect(() => {
    bodyRef.current?.scrollTo(0, bodyRef.current.scrollHeight)
  }, [lines])

  const promptText = `telemetry@${hostname}:~$`

  async function run(raw) {
    const text = raw.trim()
    setLines((l) => [...l, { kind: 'in', text: raw }])
    setInput('')
    setHIdx(-1)
    if (!text) return
    setHistory((h) => [text, ...h])

    const [cmd, ...args] = text.split(/\s+/)
    if (cmd === 'clear') { setLines([]); return }
    if (!online) {
      setLines((l) => [...l, { kind: 'err', text: 'agent is offline, command not delivered' }])
      return
    }
    setRunning(true)
    await new Promise((r) => setTimeout(r, cmd === 'ping' ? 700 : 180 + Math.random() * 220))
    const out = fakeOutput(cmd, args, { hostname })
    setLines((l) => [...l, { kind: out.endsWith('not found') ? 'err' : 'out', text: out }])
    setRunning(false)
  }

  function onKey(e) {
    if (e.key === 'Enter' && !running) run(input)
    else if (e.key === 'ArrowUp') {
      e.preventDefault()
      const i = Math.min(hIdx + 1, history.length - 1)
      if (i >= 0) { setHIdx(i); setInput(history[i]) }
    } else if (e.key === 'ArrowDown') {
      e.preventDefault()
      const i = hIdx - 1
      setHIdx(Math.max(i, -1))
      setInput(i >= 0 ? history[i] : '')
    } else if (e.key === 'l' && e.ctrlKey) {
      e.preventDefault()
      setLines([])
    }
  }

  return (
    <div className="term" onClick={() => inputRef.current?.focus()}>
      <div className="term-bar">
        <span className="term-dots"><i /><i /><i /></span>
        <span className="term-title">{hostname}: remote shell</span>
        <span className={`pill ${online ? 'pill-ok' : 'pill-off'}`}>{online ? 'connected' : 'offline'}</span>
      </div>
      <div className="term-body" ref={bodyRef}>
        {lines.map((l, i) => (
          <pre key={i} className={`term-line term-${l.kind}`}>
            {l.kind === 'in' ? <><span className="term-prompt">{promptText}</span> {l.text}</> : l.text}
          </pre>
        ))}
        <div className="term-input-row">
          <span className="term-prompt">{promptText}</span>
          <input
            ref={inputRef}
            className="term-input"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={onKey}
            spellCheck={false}
            autoComplete="off"
            aria-label="Terminal input"
            disabled={running}
          />
          {running && <span className="term-spin" />}
        </div>
      </div>
    </div>
  )
}
