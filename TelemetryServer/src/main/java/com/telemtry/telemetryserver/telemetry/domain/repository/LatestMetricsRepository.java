package com.telemtry.telemetryserver.telemetry.domain.repository;

import com.telemtry.telemetryserver.telemetry.domain.model.TelemetryBatch;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface LatestMetricsRepository {


    void save(String agentId,TelemetryBatch telemetryBatch);

    Optional<TelemetryBatch> findByAgentId(String agentId);

    List<TelemetryBatch> findAll();

    void remove(String agentId);
}
