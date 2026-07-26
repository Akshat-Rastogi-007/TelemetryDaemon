package com.telemtry.telemetryserver.agent.infrastructure.repository;

import com.telemtry.telemetryserver.agent.domain.model.Agent;
import com.telemtry.telemetryserver.agent.domain.repository.AgentRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository("inMemoryAgentRepository")
public class InMemoryAgentRepository implements AgentRepository {

    private final Map<String,Agent> inMemoryAgentRepo = new HashMap();

    @Override
    public Agent save(Agent agent) {


        return null;
    }

    @Override
    public Optional<Agent> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Agent> findAll(String userId) {
        return List.of();
    }

    @Override
    public Optional<Agent> findByInstallationId(String secureHash) {
        return Optional.empty();
    }

    @Override
    public void delete(Agent agent) {

    }

    @Override
    public boolean existsById(long id) {
        return false;
    }
}
