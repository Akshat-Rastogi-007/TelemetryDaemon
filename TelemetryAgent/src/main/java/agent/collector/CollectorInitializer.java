package agent.collector;

import agent.collector.cpu.CpuCollector;
import agent.collector.disk.DiskCollector;
import agent.collector.manager.CollectorManager;
import agent.collector.memory.MemoryCollector;
import agent.collector.scheduler.CollectorScheduler;
import agent.platform.Platform;
import agent.platform.PlatformFactory;
import agent.reporter.Reporter;
import agent.schedular.Scheduler;

import java.util.HashMap;
import java.util.List;

public class CollectorInitializer {

    private final List<Reporter> reporters;

    public CollectorInitializer(List<Reporter> reporters) {
        this.reporters = reporters;
    }

    public CollectorEngine initialize(Scheduler scheduler){

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


        CollectorScheduler collectorScheduler =
                new CollectorScheduler(
                        manager,
                        scheduler
                );


        return new CollectorEngine(
                collectorScheduler,
                reporters
        );
    }


}
