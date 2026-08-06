package com.telemtry.telemetryserver.agent.infrastructure.security;


import com.telemtry.telemetryserver.agent.api.CurrentAgentProvider;
import com.telemtry.telemetryserver.agent.domain.model.Agent;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration("currentAgentProviderImpl")
public class CurrentAgentProviderImpl implements CurrentAgentProvider {

    @Override
    public Agent currentAgent() {
        AgentPrincipal principal = (AgentPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return principal.getAgent();

    }


}
