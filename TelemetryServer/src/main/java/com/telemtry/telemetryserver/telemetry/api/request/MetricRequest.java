package com.telemtry.telemetryserver.telemetry.api.request;

import com.telemtry.telemetryserver.telemetry.domain.model.MetricUnit;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class MetricRequest {

    private String name;

    private double value;

    private MetricUnit unit;

    private Instant timestamp;

    private Map<String, String> attributes;

}
