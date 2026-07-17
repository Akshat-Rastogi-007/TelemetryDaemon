package agent.reporter.impl;

import agent.collector.Collector;
import agent.reporter.Reporter;
import agent.telemetry.Metric;
import agent.telemetry.TelemetryBatch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReporter implements Reporter {

    private final Path logDirectory;

    public FileReporter(Path logDirectory) {
        this.logDirectory = logDirectory;
    }

    @Override
    public void report(TelemetryBatch batch) {

        try {

            Files.createDirectories(logDirectory);
            Path logFile = logDirectory.resolve("telemetry.log");


            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(logFile.toFile(), true))) {

                writer.write("================================================================================");
                writer.newLine();
                writer.write("                         TELEMETRY COLLECTION CYCLE");
                writer.newLine();
                writer.write("                    " + java.time.Instant.now());
                writer.newLine();
                writer.write("================================================================================");
                writer.newLine();
                writer.newLine();

                writer.write("┌──────────────────────────────────────────────────────────────────────────────┐");
                writer.newLine();
                writer.write("│ "
                        + batch.getCollectorId().toUpperCase()
                        + " ".repeat(Math.max(0, 74 - batch.getCollectorId().length()))
                        + "│");
                writer.newLine();
                writer.write("└──────────────────────────────────────────────────────────────────────────────┘");
                writer.newLine();
                writer.newLine();

                for (Metric metric : batch.getMetrics()) {
                    writer.write(metric.toString());
                    writer.newLine();
                }

                writer.newLine();
                writer.write("================================================================================");
                writer.newLine();
                writer.write("                           COLLECTION COMPLETE");
                writer.newLine();
                writer.write("================================================================================");
                writer.newLine();
                writer.newLine();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
