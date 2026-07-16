package agent.telemetry;

import java.time.Instant;
import java.util.Map;

public class Metric {

    private String name;
    private double value;
    private MetricUnit unit;
    private Instant timestamp;
    private Map<String, String> attributes;

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public MetricUnit getUnit() {
        return unit;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Metric(String name, double value, MetricUnit unit, Instant timeStamp, Map<String, String> attributes) {
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.timestamp = timeStamp;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "Metric{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", unit=" + unit +
                ", timestamp=" + timestamp +
                ", attributes=" + attributes +
                '}';
    }
}
