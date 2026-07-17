package agent.schedular;

import agent.collector.Collector;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogSchedular implements Scheduler{


    private final ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor();

    @Override
    public void start() {

        System.out.println("*****Starting SCHEDULER*****");

        // start the collectors and stuff
        // not needed right now

    }

    @Override
    public void stop() {

        // stop the collectors

        System.out.println("**Stopping the Executor**");
        executor.shutdown();
        System.out.println("**Executor Stopped**");

    }
    @Override
    public void schedule(Collector collector, Duration interval) {

        executor.scheduleAtFixedRate(
                () -> {

                    try {

                        Path logDirectory = Paths.get("logs");
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
                                    + collector.getId().toUpperCase()
                                    + " ".repeat(Math.max(0, 74 - collector.getId().length()))
                                    + "│");
                            writer.newLine();
                            writer.write("└──────────────────────────────────────────────────────────────────────────────┘");
                            writer.newLine();
                            writer.newLine();

                            for (var metric : collector.collect()) {
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

                },
                0,
                interval.getSeconds(),
                TimeUnit.SECONDS
        );
    }
}
