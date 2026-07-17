package agent.telemetry;

import java.time.Instant;
import java.util.Collection;

public class TelemetryBatch {

    private final String collectorId;
    private final Instant timestamp;
    private final Collection<Metric> metrics;

    public String getCollectorId() {
        return collectorId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Collection<Metric> getMetrics() {
        return metrics;
    }

    public TelemetryBatch(String collectorId, Instant timestamp, Collection<Metric> metrics) {
        this.collectorId = collectorId;
        this.timestamp = timestamp;
        this.metrics = metrics;
    }
}
