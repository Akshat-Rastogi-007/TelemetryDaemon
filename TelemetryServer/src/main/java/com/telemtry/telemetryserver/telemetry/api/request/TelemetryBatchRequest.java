package com.telemtry.telemetryserver.telemetry.api.request;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class TelemetryBatchRequest {


    private Instant timestamp;

    private List<MetricRequest> metrics = new ArrayList<>();


    @Override
    public String toString() {
        return "TelemetryBatchRequest{" +
                ", timestamp=" + timestamp +
                ", metrics=" + metrics +
                '}';
    }
}
