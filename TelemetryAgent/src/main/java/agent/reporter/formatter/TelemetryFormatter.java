package agent.reporter.formatter;

import agent.telemetry.TelemetryBatch;

public class TelemetryFormatter {

    public String telemetryFormatter(TelemetryBatch batch) {

        StringBuilder builder = new StringBuilder();

        builder.append(System.lineSeparator());
        builder.append(
                "================================================================================"
        ).append(System.lineSeparator());

        builder.append(
                "                         TELEMETRY COLLECTION CYCLE"
        ).append(System.lineSeparator());

        builder.append(
                        "                    "
                ).append(batch.getTimestamp())
                .append(System.lineSeparator());

        builder.append(
                "================================================================================"
        ).append(System.lineSeparator());

        builder.append(System.lineSeparator());

        batch.getMetricMap().forEach((collectorId, metrics) -> {

            builder.append(
                    "┌──────────────────────────────────────────────────────────────────────────────┐"
            ).append(System.lineSeparator());

            builder.append(
                    String.format(
                            "│ %-76s │%n",
                            collectorId.toUpperCase()
                    )
            );

            builder.append(
                    "└──────────────────────────────────────────────────────────────────────────────┘"
            ).append(System.lineSeparator());

            builder.append(System.lineSeparator());

            metrics.forEach(metric ->
                    builder.append(metric)
                            .append(System.lineSeparator())
            );

            builder.append(System.lineSeparator());
        });

        builder.append(
                "================================================================================"
        ).append(System.lineSeparator());

        builder.append(
                "                           COLLECTION COMPLETE"
        ).append(System.lineSeparator());

        builder.append(
                "================================================================================"
        ).append(System.lineSeparator());

        builder.append(System.lineSeparator());

        return builder.toString();
    }
}