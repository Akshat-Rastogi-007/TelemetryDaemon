package com.telemtry.telemetryserver.telemetry.api.request;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class TelemetryBatchRequest {


    private String collectorId;
    private Instant timestamp;

    private List<MetricRequest> metrics = new ArrayList<>();


    @Override
    public String toString() {
        return "TelemetryBatchRequest{" +
                "collectorId='" + collectorId + '\'' +
                ", timestamp=" + timestamp +
                ", metrics=" + metrics +
                '}';
    }
}
