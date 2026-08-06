package com.telemtry.telemetryserver.agent.api;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public interface AgentAuthenticator {

    Authentication authenticate(String token);

}
