package com.telemtry.telemetryserver.common.security.hash;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface AgentCredentialHasher {

    String getHash(String secureKey);

}
