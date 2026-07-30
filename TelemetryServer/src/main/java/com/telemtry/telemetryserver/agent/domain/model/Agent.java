package com.telemtry.telemetryserver.agent.domain.model;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String publicId;

    private String deviceName;
    private AuthType authType;
    private float version;
    private AgentStatus status;

    private String secureHash;

    private String installationId;

    @Embedded
    private SystemInfo systemInfo;

    private long userId;

    @CreationTimestamp
    private LocalDateTime registeredAt;
    private LocalDateTime lastSeenAt;
    private LocalDateTime lastHeartBeat;


    @PrePersist
    public void generateId() {
        if (this.publicId == null) {
            this.publicId = "ag_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        }
    }

}
