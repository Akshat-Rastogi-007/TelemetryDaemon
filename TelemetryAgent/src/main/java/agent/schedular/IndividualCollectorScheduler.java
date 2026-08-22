package agent.schedular;

import agent.collector.Collector;
import agent.reporter.Reporter;
import agent.telemetry.Metric;
import agent.telemetry.TelemetryBatch;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IndividualCollectorScheduler implements Scheduler {

    private final List<Reporter> reporters;


    public IndividualCollectorScheduler(List<Reporter> reporters) {
        this.reporters = reporters;

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
    public void schedule(Collector collector, Duration interval) {

        // task which we need to schedule


        System.out.println("*****Inside Schedular*****");

        executor.scheduleAtFixedRate(
                () -> {

                    try {

                        Collection<Metric> metrics = collector.collect();

                        Map<String, Collection<Metric>> metricMap = new HashMap<>();


                        metrics.forEach(metric -> {

                            metricMap.put(collector.getId(),metrics);
                        });

                        TelemetryBatch batch = new TelemetryBatch(
                                Instant.now(),
                                metricMap
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

                },
                0,
                interval.getSeconds(),
                TimeUnit.SECONDS);
    }
}
