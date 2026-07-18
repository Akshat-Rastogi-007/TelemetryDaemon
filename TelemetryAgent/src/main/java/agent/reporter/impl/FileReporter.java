package agent.reporter.impl;

import agent.collector.Collector;
import agent.reporter.Reporter;
import agent.reporter.formatter.TelemetryFormatter;
import agent.telemetry.Metric;
import agent.telemetry.TelemetryBatch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReporter implements Reporter {

    private final TelemetryFormatter formatter = new TelemetryFormatter();

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

                writer.write(formatter.telemetryFormatter(batch));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
