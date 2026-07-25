package com.telemtry.telemetryserver.agent.infrastructure.repository.jpa;

import com.telemtry.telemetryserver.agent.domain.model.Agent;
import com.telemtry.telemetryserver.agent.domain.repository.AgentRepository;
import com.telemtry.telemetryserver.agent.infrastructure.repository.AgentJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository("jpaAgentRepository")
public class JpaAgentRepository implements AgentRepository {

    private final AgentJpaRepository repository;

    public JpaAgentRepository(AgentJpaRepository repository) {
        this.repository = repository;
    }


    @Override
    public Agent save(Agent agent) {

        return repository.save(agent);

    }

    @Override
    public Optional<Agent> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Agent> findAll(String userId) {

        return repository.findAll()
                .stream()
                .filter(agent -> Objects.equals(agent.getUserId(), userId))
                .toList();
    }

    @Override
    public void delete(Agent agent) {

        repository.delete(agent);

    }

    @Override
    public boolean existsById(long id) {

        return findById(id).isPresent();

    }
}
