package com.telemtry.telemetryserver.telemetry.infrastructure.repository;

import com.telemtry.telemetryserver.telemetry.domain.model.TelemetryBatch;
import com.telemtry.telemetryserver.telemetry.domain.repository.LatestMetricsRepository;
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@Repository
public class InMemoryLatestMetricsRepository implements LatestMetricsRepository {


    private final Map<Long,TelemetryBatch> inMemoryMap = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public TelemetryBatch save(Long agentId,TelemetryBatch telemetryBatch) {

        long id = idGenerator.incrementAndGet();

        telemetryBatch.setId(id);

        inMemoryMap.put(agentId, telemetryBatch);

        return telemetryBatch;
    }

    @Override
    public Optional<TelemetryBatch> findByAgentId(Long agentId) {
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
    public void remove(Long agentId) {

        inMemoryMap.remove(agentId);


    }
}
