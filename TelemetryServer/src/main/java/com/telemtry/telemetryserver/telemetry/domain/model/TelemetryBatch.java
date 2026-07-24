package com.telemtry.telemetryserver.telemetry.domain.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class TelemetryBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String collectorId;
    private Instant timestamp;

    @OneToMany(
            mappedBy = "telemetryBatch",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Metric> metrics = new ArrayList<>();


}
