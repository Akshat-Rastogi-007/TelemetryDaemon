package com.telemtry.telemetryserver.telemetry.infrastructure.repository;

import com.telemtry.telemetryserver.telemetry.domain.model.TelemetryBatch;
import com.telemtry.telemetryserver.telemetry.domain.repository.LatestMetricsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class InMemoryLatestMetricsRepository implements LatestMetricsRepository {


    private final Map<String,TelemetryBatch> inMemoryMap = new ConcurrentHashMap<>();


    @Override
    public void save(String agentId,TelemetryBatch telemetryBatch) {

        inMemoryMap.put(agentId,telemetryBatch);

    }

    @Override
    public Optional<TelemetryBatch> findByAgentId(String agentId) {
        TelemetryBatch telemetryBatch = inMemoryMap.get(agentId);
        return Optional.ofNullable(telemetryBatch);
    }

    @Override
    public List<TelemetryBatch> findAll() {

        return inMemoryMap.values()
                .stream()
                .toList();

    }

    @Override
    public void remove(String agentId) {

        inMemoryMap.remove(agentId);


    }
}
