package com.telemtry.telemetryserver.agent.infrastructure.secureKeyGenerator;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface AgentCredentialGenerator {

    String generateSecureKey();


}
