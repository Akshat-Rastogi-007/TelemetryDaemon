package com.telemtry.telemetryserver.agent.infrastructure.security;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface AgentCredentialGenerator {

    String generateSecureKey();


}
