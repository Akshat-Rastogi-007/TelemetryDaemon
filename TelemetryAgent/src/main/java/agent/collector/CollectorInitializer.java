package agent.collector;

import agent.collector.cpu.CpuCollector;
import agent.collector.disk.DiskCollector;
import agent.collector.manager.CollectorManager;
import agent.collector.memory.MemoryCollector;
import agent.collector.scheduler.CollectorScheduler;
import agent.platform.Platform;
import agent.platform.PlatformFactory;
import agent.reporter.Reporter;
import agent.schedular.BatchCollectorScheduler;
import agent.schedular.Scheduler;
import agent.telemetry.aggregator.AggregatorFlusher;
import agent.telemetry.aggregator.TelemetryAggregator;

import java.util.HashMap;
import java.util.List;

public class CollectorInitializer {


    public CollectorEngine initialize( List<Reporter> reporters){

        Platform platform =
                new PlatformFactory().create();


        CollectorManager manager =
                new CollectorManager(
                        new HashMap<>()
                );


        List<Collector> collectors = List.of(
                new CpuCollector(platform.cpu()),
                new DiskCollector(platform.disk()),
                new MemoryCollector(platform.memory())
        );


        manager.registerCollector(collectors);

        CollectorScheduler collectorScheduler = getScheduler(reporters, manager);

        return new CollectorEngine(
                collectorScheduler
        );
    }

    private CollectorScheduler getScheduler(List<Reporter> reporters, CollectorManager manager) {

        TelemetryAggregator telemetryAggregator = new TelemetryAggregator(manager.getActiveCollectorCount());

        telemetryAggregator.startCycle();

        BatchCollectorScheduler scheduler = new BatchCollectorScheduler(telemetryAggregator);

        AggregatorFlusher aggregatorFlusher = new AggregatorFlusher(telemetryAggregator, reporters);

        telemetryAggregator.addObserver(aggregatorFlusher);

        return new CollectorScheduler(
                manager,
                scheduler
        );
    }


}
