package com.telemtry.telemetryserver.telemetry.domain.repository;

import com.telemtry.telemetryserver.telemetry.domain.model.TelemetryBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelemetryBatchRepository extends JpaRepository<TelemetryBatch, Long> {
}
