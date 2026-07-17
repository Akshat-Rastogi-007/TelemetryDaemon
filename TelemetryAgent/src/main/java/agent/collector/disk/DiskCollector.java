package agent.collector.disk;

import agent.collector.Collector;
import agent.platform.disk.DiskProvider;
import agent.platform.disk.DiskSnapshot;
import agent.telemetry.Metric;
import agent.telemetry.MetricUnit;

import java.time.Instant;
import java.util.*;

public class DiskCollector implements Collector {

    private final DiskProvider diskProvider;

    public DiskCollector(DiskProvider diskProvider) {
        this.diskProvider = diskProvider;
    }

    @Override
    public String getId() {
        return "disk";
    }

    @Override
    public Collection<Metric> collect() {


        System.out.println("**INSIDE Disk Collector**");

        Collection<Metric> metrics = new ArrayList<>();

        Instant now = Instant.now();

        Collection<DiskSnapshot> snapshotList = diskProvider.snapshot();

        for (DiskSnapshot snapshot : snapshotList) {

            Map<String, String> attributes = new HashMap<>();
            attributes.put("disk", snapshot.getName());

            metrics.add(new Metric(
                    "disk.total",
                    snapshot.getTotalSpace(),
                    MetricUnit.BYTE,
                    now,
                    attributes
            ));

            metrics.add(new Metric(
                    "disk.used",
                    snapshot.getUsedSpace(),
                    MetricUnit.BYTE,
                    now,
                    attributes
            ));

            metrics.add(new Metric(
                    "disk.free",
                    snapshot.getFreeSpace(),
                    MetricUnit.BYTE,
                    now,
                    attributes
            ));

            metrics.add(new Metric(
                    "disk.usable",
                    snapshot.getUsableSpace(),
                    MetricUnit.BYTE,
                    now,
                    attributes
            ));
        }

        return metrics;

    }
}
