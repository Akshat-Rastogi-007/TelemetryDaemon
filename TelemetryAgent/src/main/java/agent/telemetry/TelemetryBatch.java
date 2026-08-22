package agent.telemetry;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;

public class TelemetryBatch {

    private final Instant timestamp;
    private final Map<String, Collection<Metric>> metricMap;


    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, Collection<Metric>> getMetricMap() {
        return metricMap;
    }

        public TelemetryBatch( Instant timestamp, Map<String, Collection<Metric>> metricMap) {
        this.timestamp = timestamp;
        this.metricMap = metricMap;
    }

    @Override
    public String toString() {
        return "TelemetryBatch{" +
                ", timestamp=" + timestamp +
                ", aggregatedMetrics=" + metricMap +
                '}';
    }
}
