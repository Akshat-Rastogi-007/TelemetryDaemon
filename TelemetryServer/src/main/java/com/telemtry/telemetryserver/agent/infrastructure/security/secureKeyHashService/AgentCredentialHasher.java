package com.telemtry.telemetryserver.agent.infrastructure.security.secureKeyHashService;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface AgentCredentialHasher {

    String getHash(String secureKey);

}
