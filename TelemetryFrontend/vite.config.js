import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Port 3000 matches the origin allowed in the server's CorsConfig.
export default defineConfig({
  plugins: [react()],
  server: { port: 3000, strictPort: true },
})
