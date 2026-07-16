package agent.collector;

import agent.telemetry.Metric;

import java.util.Collection;

public interface Collector {

    String getId();
    Collection<Metric> collect();
}
