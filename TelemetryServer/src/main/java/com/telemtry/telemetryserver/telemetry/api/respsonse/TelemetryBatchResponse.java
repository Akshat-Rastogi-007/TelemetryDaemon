package com.telemtry.telemetryserver.telemetry.api.respsonse;

import com.telemtry.telemetryserver.telemetry.domain.model.Metric;
import lombok.Data;

import java.time.Instant;
import java.util.List;


@Data
public class TelemetryBatchResponse {

    private String collectorId;

    private Instant timestamp;

    private List<Metric> metrics;

    @Override
    public String toString() {
        return "TelemetryBatchResponse{" +
                "collectorId='" + collectorId + '\'' +
                ", timestamp=" + timestamp +
                ", metrics=" + metrics +
                '}';
    }
}
