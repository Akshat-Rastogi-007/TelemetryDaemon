package com.telemtry.telemetryserver.agent.domain.repository;

import com.telemtry.telemetryserver.agent.domain.model.Collector;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectorRepository {

    List<Collector> findByAgentId(Long agentId);

    Optional<Collector> findByAgentIdAndCollectorId(
            Long agentId,
            String collectorId
    );

    boolean existsByAgentIdAndCollectorId(
            Long agentId,
            String collectorId
    );

    void deleteByAgentIdAndCollectorId(
            Long agentId,
            String collectorId
    );
}
