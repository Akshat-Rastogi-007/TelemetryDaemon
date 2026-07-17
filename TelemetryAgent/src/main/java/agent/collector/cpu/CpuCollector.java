package agent.collector.cpu;

import agent.collector.Collector;
import agent.platform.cpu.CpuProvider;
import agent.platform.cpu.CpuSnapshot;
import agent.telemetry.Metric;
import agent.telemetry.MetricUnit;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CpuCollector implements Collector {

    private final CpuProvider cpuProvider;

    public CpuCollector(CpuProvider cpuProvider) {
        this.cpuProvider = cpuProvider;
    }

    @Override
    public String getId() {
        return "cpu";
    }

    @Override
    public Collection<Metric> collect() {

        System.out.println("*****Collecting Data*****");

        Instant now = Instant.now();
        CpuSnapshot snapshot = cpuProvider.snapshot();
        Map<String, String> attributes = Map.of(
                "source", "cpu.mxbean"
        );

        return List.of(
                new Metric("cpu.usage", snapshot.getCpuUsage()*100, MetricUnit.PERCENT,now,attributes)
                , new Metric("logical.processors",snapshot.getLogicalProcessors(),MetricUnit.COUNT,now,attributes)
                , new Metric("load.average",snapshot.getLoadAverage(),MetricUnit.COUNT,now,attributes)
        );

    }
}
