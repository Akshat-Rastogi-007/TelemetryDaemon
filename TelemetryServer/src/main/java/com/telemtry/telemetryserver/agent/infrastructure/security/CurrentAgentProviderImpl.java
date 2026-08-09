package com.telemtry.telemetryserver.agent.infrastructure.security;


import com.telemtry.telemetryserver.agent.api.CurrentAgentProvider;
import com.telemtry.telemetryserver.agent.domain.model.Agent;
import com.telemtry.telemetryserver.common.exception.AgentExceptionError;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration("currentAgentProviderImpl")
public class CurrentAgentProviderImpl implements CurrentAgentProvider {

    @Override
    public Agent currentAgent() {
        
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof AgentPrincipal principal)) {
            throw new AgentExceptionError("No authenticated agent found.");
        }

        return principal.getAgent();
    }


}
