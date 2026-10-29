package com.telemtry.telemetryserver.agent.infrastructure.repository.jpa;

import com.telemtry.telemetryserver.agent.domain.model.Collector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectorJpaRepository extends JpaRepository<Collector, String> {




}
