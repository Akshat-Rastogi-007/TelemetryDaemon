package com.telemtry.telemetryserver.agent.api.response;

import lombok.Data;

@Data
public class AgentResponseDto {

    private Long agentId;
    private String secureKey;
}
