package com.telemtry.telemetryserver.user.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PersonalAccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
}
