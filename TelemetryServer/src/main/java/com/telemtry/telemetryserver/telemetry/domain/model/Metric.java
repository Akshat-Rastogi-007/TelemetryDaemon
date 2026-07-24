package com.telemtry.telemetryserver.telemetry.domain.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "metrics")
@Data
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double value;

    @Enumerated(EnumType.STRING)
    private MetricUnit unit;

    private Instant timestamp;

    @ElementCollection
    @CollectionTable(
            name = "metric_attributes",
            joinColumns = @JoinColumn(name = "metric_id")
    )
    @MapKeyColumn(name = "attribute_key")
    @Column(name = "attribute_value")
    private Map<String, String> attributes = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "telemetry_batch_id")
    private TelemetryBatch telemetryBatch;
}

