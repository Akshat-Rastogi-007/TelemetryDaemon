package com.telemtry.telemetryserver.agent.domain.model;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String deviceName;
    private AuthType authType;
    private float version;
    private AgentStatus status;

    private String secureHash;

    private String installationId;

    @Embedded
    private SystemInfo systemInfo;

    private String userId; // need to change

    private LocalDateTime registeredAt;
    private LocalDateTime lastSeenAt;
    private LocalDateTime lastHeartBeat;


}
