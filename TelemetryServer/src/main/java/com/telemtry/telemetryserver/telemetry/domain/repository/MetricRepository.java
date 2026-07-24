package com.telemtry.telemetryserver.telemetry.domain.repository;

import com.telemtry.telemetryserver.telemetry.domain.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {


}
