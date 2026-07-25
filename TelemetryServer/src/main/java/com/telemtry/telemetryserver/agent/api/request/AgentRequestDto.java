package com.telemtry.telemetryserver.agent.api.request;

import com.telemtry.telemetryserver.agent.domain.model.AgentStatus;
import com.telemtry.telemetryserver.agent.domain.model.AuthType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgentRequestDto {


    private String deviceName;
    private AuthType authType;
    private float version;

    private String secureHash;
    private String installationId;

    private String javaVersion;
    private String operatingSystem;
    private String architecture;
    private LocalDateTime registeredAt;
    private LocalDateTime lastSeenAt;
    private LocalDateTime lastHeartBeat;

}
