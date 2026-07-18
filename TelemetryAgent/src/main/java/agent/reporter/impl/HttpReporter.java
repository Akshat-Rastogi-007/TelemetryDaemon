package agent.reporter.impl;

import agent.reporter.Reporter;
import agent.telemetry.TelemetryBatch;
import agent.transport.TelemetryTransport;

public class HttpReporter implements Reporter {

    private final TelemetryTransport transport;

    public HttpReporter(TelemetryTransport transport) {
        this.transport = transport;
    }

    @Override
    public void report(TelemetryBatch batch) {

        transport.send(batch);

    }
}
