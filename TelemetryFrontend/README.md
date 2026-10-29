# Telemetry Frontend

React + Vite dashboard for the Telemetry Server.

```bash
npm install
npm run dev        # http://localhost:3000 (the origin allowed by the server's CorsConfig)
```

Set `VITE_API_URL` in `.env.local` if the server isn't on `http://localhost:8080`.

**Demo mode:** "Explore with demo data" on the login page runs the whole UI on fake agents and metrics, with no server needed.

## Flow
1. Log in or create an account
2. Generate a Personal Access Token (shown once)
3. Run `java -jar target/TelemetryAgent-1.0-SNAPSHOT.jar login`, then `start`, from `TelemetryAgent/`
4. The agent appears on the home page. Open it to see live CPU, memory and disk telemetry (polled every 3s) and a terminal

The terminal is simulated. The server has no command-execution API yet.
