package com.telemtry.telemetryserver.agent.infrastructure.repository;

import com.telemtry.telemetryserver.agent.domain.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentJpaRepository extends JpaRepository<Agent,Long> {
}
