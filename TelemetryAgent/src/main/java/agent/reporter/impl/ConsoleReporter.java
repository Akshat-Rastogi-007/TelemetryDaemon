package agent.reporter.impl;

import agent.reporter.Reporter;
import agent.telemetry.Metric;
import agent.telemetry.TelemetryBatch;


public class ConsoleReporter implements Reporter {


    @Override
    public void report(TelemetryBatch batch) {

        System.out.println();
        System.out.println("================================================================================");
        System.out.println("                         TELEMETRY COLLECTION CYCLE");
        System.out.println("                    " + batch.getTimestamp());
        System.out.println("================================================================================");
        System.out.println();

        System.out.println("┌──────────────────────────────────────────────────────────────────────────────┐");
        System.out.printf(
                "│ %-76s │%n",
                batch.getCollectorId().toUpperCase()
        );
        System.out.println("└──────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();

        for (Metric metric : batch.getMetrics()) {
            System.out.println(metric);
        }

        System.out.println();
        System.out.println("================================================================================");
        System.out.println("                           COLLECTION COMPLETE");
        System.out.println("================================================================================");
        System.out.println();
    }
}
