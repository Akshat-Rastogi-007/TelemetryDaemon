package com.telemtry.telemetryserver.agent.infrastructure.repository.jpa;

import com.telemtry.telemetryserver.agent.domain.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentJpaRepository extends JpaRepository<Agent,Long> {

    Optional<Agent> findAgentByInstallationId(String installationId);

    List<Agent> findAgentsByUserId(long userId);
}
