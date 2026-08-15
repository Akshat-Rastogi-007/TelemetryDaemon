package agent.reporter.impl;

import agent.reporter.Reporter;
import agent.telemetry.TelemetryBatch;
import agent.transport.dispatcher.TelemetryDispatcher;

public class HttpReporter implements Reporter {


    private final TelemetryDispatcher telemetryDispatcher;
    public HttpReporter( TelemetryDispatcher telemetryDispatcher) {
        this.telemetryDispatcher = telemetryDispatcher;
    }

    @Override
    public void report(TelemetryBatch batch) {

        telemetryDispatcher.dispatch(batch);

    }
}
