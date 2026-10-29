// Minimal SVG line chart for a rolling window of values.
export default function Sparkline({ values, max = 100, height = 56 }) {
  const w = 240
  if (values.length < 2) return <svg className="spark" viewBox={`0 0 ${w} ${height}`} preserveAspectRatio="none" />
  const top = Math.max(max, ...values)
  const step = w / (values.length - 1)
  const pts = values.map((v, i) => [i * step, height - (v / top) * (height - 4) - 2])
  const line = pts.map(([x, y], i) => `${i ? 'L' : 'M'}${x.toFixed(1)},${y.toFixed(1)}`).join(' ')
  const area = `${line} L${w},${height} L0,${height} Z`
  return (
    <svg className="spark" viewBox={`0 0 ${w} ${height}`} preserveAspectRatio="none" aria-hidden="true">
      <path d={area} className="spark-area" />
      <path d={line} className="spark-line" vectorEffect="non-scaling-stroke" />
    </svg>
  )
}
