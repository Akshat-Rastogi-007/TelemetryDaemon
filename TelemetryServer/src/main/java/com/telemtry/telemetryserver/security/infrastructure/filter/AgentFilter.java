package com.telemtry.telemetryserver.security.infrastructure.filter;

import com.telemtry.telemetryserver.agent.api.AgentAuthenticator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AgentFilter extends OncePerRequestFilter {

    private final AgentAuthenticator agentAuthenticator;

    public AgentFilter(
            @Qualifier("agentAuthenticationService")
            AgentAuthenticator agentAuthenticator) {
        this.agentAuthenticator = agentAuthenticator;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    )
            throws ServletException, IOException {


        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String rawToken = authorizationHeader.substring(7);

        if (!rawToken.startsWith("agt_")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authenticate = agentAuthenticator.authenticate(rawToken);

        SecurityContextHolder.getContext().setAuthentication(authenticate);


        filterChain.doFilter(request,response);

    }
}
