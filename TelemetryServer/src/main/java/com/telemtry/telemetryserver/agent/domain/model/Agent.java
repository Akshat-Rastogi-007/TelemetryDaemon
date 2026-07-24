package com.telemtry.telemetryserver.agent.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String deviceName;
    private AuthType authType;
    private float version;
    private AgentStatus status;
    @Embedded
    private SystemInfo systemInfo;

    private String userId; // need to change

    private LocalDateTime registeredAt;
    private LocalDateTime lastSeenAt;
    private LocalDateTime lastHeartBeat;


}
