package com.telemtry.telemetryserver.telemetry.api.request;

import lombok.Data;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TelemetryBatchRequest {


    private Instant timestamp;

    private Map<String, List<MetricRequest>> metricMap = new HashMap<>();

    @Override
    public String toString() {
        return "TelemetryBatchRequest{" +
                "timestamp=" + timestamp +
                ", metrics=" + metricMap +
                '}';
    }
}
