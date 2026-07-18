package agent.transport;

import agent.telemetry.TelemetryBatch;

public interface TelemetryTransport {

    void send(TelemetryBatch telemetryBatch);
}
