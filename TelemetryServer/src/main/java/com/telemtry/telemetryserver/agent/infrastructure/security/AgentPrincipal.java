package com.telemtry.telemetryserver.agent.infrastructure.security;

import com.telemtry.telemetryserver.agent.domain.model.Agent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AgentPrincipal implements UserDetails {

    private final Agent agent;

    public AgentPrincipal(Agent agent) {
        this.agent = agent;
    }

    public Agent getAgent(){

        return this.agent;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
