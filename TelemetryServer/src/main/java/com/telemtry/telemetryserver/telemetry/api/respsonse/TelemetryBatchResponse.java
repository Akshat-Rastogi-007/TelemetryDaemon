package com.telemtry.telemetryserver.telemetry.api.respsonse;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class TelemetryBatchResponse {

    private Instant timestamp;

    private Long agentId;

    private List<CollectorMetricsResponse> collectorMetrics = new ArrayList<>();
}
