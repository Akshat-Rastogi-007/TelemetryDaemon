package com.telemtry.telemetryserver.agent.infrastructure.security;

import com.telemtry.telemetryserver.agent.api.AgentAuthenticator;
import com.telemtry.telemetryserver.agent.application.AgentService;
import com.telemtry.telemetryserver.agent.domain.model.Agent;
import com.telemtry.telemetryserver.common.exception.AgentExceptionError;
import com.telemtry.telemetryserver.common.infrastructure.hash.Hasher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service("agentAuthenticationService")
public class AgentAuthenticationService implements AgentAuthenticator {

    private final AgentService agentService;
    private final Hasher hasher;


    public AgentAuthenticationService(AgentService agentService,
                                      @Qualifier("sha256Hasher")
                                      Hasher hasher) {
        this.agentService = agentService;
        this.hasher = hasher;
    }


    @Override
    public Authentication authenticate(String token) {


        String secureToken = hasher.getHash(token);

        Agent agent = agentService
                .findBySecureToken(secureToken)
                .orElseThrow(
                        () -> new AgentExceptionError("Kindly RELOGIN THE AGENT")
                );

        AgentPrincipal agentPrincipal = new AgentPrincipal(agent);

        return new AgentAuthenticationToken(agentPrincipal);


    }
}
