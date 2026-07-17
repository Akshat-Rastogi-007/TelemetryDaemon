package agent.schedular;

import agent.collector.Collector;
import agent.reporter.Reporter;
import agent.telemetry.Metric;
import agent.telemetry.TelemetryBatch;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultSchedular implements Scheduler {

    public DefaultSchedular() {

        System.out.println("CREATING SCHEDULAR");
    }

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
    public void schedule(Collector collector, List<Reporter> reporters,Duration interval) {

        // task which we need to schedule


        System.out.println("*****Inside Schedular*****");

        Collection<Metric> collectionMetric = collector.collect();

        executor.scheduleAtFixedRate(
                () -> {

                    try {

                        Collection<Metric> metrics = collector.collect();

                        TelemetryBatch batch = new TelemetryBatch(
                                collector.getId(),
                                Instant.now(),
                                metrics
                        );


                        for (Reporter reporter : reporters) {

                            reporter.report(batch);
                        }

                    } catch (Exception e) {

                        System.err.println(
                                "Collector '" + collector.getId()
                                        + "' failed: " + e.getMessage());

                        e.printStackTrace();

                    }

                }
                ,
                0,
                interval.getSeconds(),
                TimeUnit.SECONDS);

    }
}
