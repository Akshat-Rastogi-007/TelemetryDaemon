package com.telemtry.telemetryserver.agent.api.response;

import lombok.Data;

@Data
public class AgentResponseDto {
    private String agentId;
    private String secureKey;
    private String installationId;
}
