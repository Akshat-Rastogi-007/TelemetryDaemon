package agent.collector.memory;

import agent.collector.Collector;
import agent.platform.memory.MemoryProvider;
import agent.platform.memory.MemorySnapshot;
import agent.telemetry.Metric;
import agent.telemetry.MetricUnit;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MemoryCollector implements Collector {

    private final MemoryProvider memoryProvider;

    public MemoryCollector(MemoryProvider memoryProvider) {
        this.memoryProvider = memoryProvider;
    }

    @Override
    public String getId() {
        return "memory";
    }

    @Override
    public Collection<Metric> collect() {

        MemorySnapshot snapshot = memoryProvider.snapshot();
        Instant now = Instant.now();
        Map<String, String> attributes = Map.of(
                "source", "memory.mxbean"
        );

        double usedMemoryPercentage = (double) snapshot.getUsedMemory() / snapshot.getTotalMemory() * 100;
        double usedSwapMemoryPercentage = (double) snapshot.getUsedMemory() / snapshot.getTotalMemory() * 100;


        return List.of(
                new Metric("total.memory", snapshot.getTotalMemory(), MetricUnit.BYTE,now,attributes)
                , new Metric("free.memory",snapshot.getFreeMemory(),MetricUnit.BYTE,now,attributes)
                , new Metric("used.memory",snapshot.getUsedMemory(),MetricUnit.COUNT,now,attributes)
                , new Metric("used.memory.percentage", usedMemoryPercentage,MetricUnit.PERCENT,now,attributes),
                new Metric("total.swap.memory", snapshot.getTotalSwap(), MetricUnit.BYTE,now,attributes)
                , new Metric("free.swap.memory",snapshot.getFreeSwap(),MetricUnit.BYTE,now,attributes)
                , new Metric("used.swap.memory",snapshot.getUsedSwap(),MetricUnit.COUNT,now,attributes)
                , new Metric("used.memory.percentage", usedSwapMemoryPercentage,MetricUnit.PERCENT,now,attributes)
        );

    }
}
