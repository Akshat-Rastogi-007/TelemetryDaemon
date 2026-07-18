package agent.reporter.impl;

import agent.reporter.Reporter;
import agent.reporter.formatter.TelemetryFormatter;
import agent.telemetry.Metric;
import agent.telemetry.TelemetryBatch;


public class ConsoleReporter implements Reporter {

    private final TelemetryFormatter formatter = new TelemetryFormatter();

    @Override
    public void report(TelemetryBatch batch) {

        System.out.println(formatter.telemetryFormatter(batch));
    }
}
