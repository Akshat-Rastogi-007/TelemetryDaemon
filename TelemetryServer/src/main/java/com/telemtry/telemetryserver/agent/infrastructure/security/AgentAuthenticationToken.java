package com.telemtry.telemetryserver.agent.infrastructure.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;


public class AgentAuthenticationToken extends AbstractAuthenticationToken {


    private final AgentPrincipal agentPrincipal;

    public AgentAuthenticationToken(AgentPrincipal agentPrincipal) {

        super(agentPrincipal.getAuthorities());
        this.agentPrincipal = agentPrincipal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return agentPrincipal;
    }
}
