package com.telemtry.telemetryserver.agent.domain.repository;

import com.telemtry.telemetryserver.agent.domain.model.Agent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository {

    Agent save(Agent agent);

    Optional<Agent> findById(long id);

    List<Agent> findAll(String userId);

    Optional<Agent> findByInstallationId(String secureHash);

    void delete(Agent agent);

    void delete(String id);

    boolean existsById(long id);

    List<Agent> findAgentsByUserId(long userId);

    void deleteAll();

    Optional<Agent> findBySecureToken(String secureToken);

    Optional<Agent> findByPublicId(String publicId);
}
