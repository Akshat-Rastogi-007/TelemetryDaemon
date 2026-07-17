package agent.reporter;

import agent.collector.Collector;
import agent.telemetry.Metric;
import agent.telemetry.TelemetryBatch;

import java.util.Collection;

public interface Reporter {

    void report(TelemetryBatch batch);

}
