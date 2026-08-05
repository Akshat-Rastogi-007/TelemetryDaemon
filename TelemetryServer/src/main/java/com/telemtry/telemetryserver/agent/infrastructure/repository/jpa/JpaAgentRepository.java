package com.telemtry.telemetryserver.agent.infrastructure.repository.jpa;

import com.telemtry.telemetryserver.agent.domain.model.Agent;
import com.telemtry.telemetryserver.agent.domain.repository.AgentRepository;
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
    public Optional<Agent> findByInstallationId(String installationId) {

        return repository.findAgentByInstallationId(installationId);

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

    @Override
    public List<Agent> findAgentsByUserId(long userId) {
        return repository.findAgentsByUserId(userId);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public Optional<Agent> findBySecureToken(String secureToken) {
        return repository.findAgentBySecureHashToken(secureToken);
    }
}
