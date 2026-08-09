package com.telemtry.telemetryserver.security.infrastructure.filter;

import com.telemtry.telemetryserver.agent.api.AgentAuthenticator;
import com.telemtry.telemetryserver.agent.api.CurrentAgentProvider;
import com.telemtry.telemetryserver.user.api.CurrentUserProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AgentFilter extends OncePerRequestFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(AgentFilter.class);
    private final AgentAuthenticator agentAuthenticator;
    private final CurrentAgentProvider currentAgentProvider;

    public AgentFilter(
            @Qualifier("agentAuthenticationService")
            AgentAuthenticator agentAuthenticator,
            CurrentAgentProvider currentAgentProvider){
        this.agentAuthenticator = agentAuthenticator;
        this.currentAgentProvider = currentAgentProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    )
            throws ServletException, IOException {



        logger.debug(
                "Processing agent authentication for {} {}",
                request.getMethod(),
                request.getRequestURI()
        );
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.debug("No Authorization header present.");
            filterChain.doFilter(request, response);
            return;
        }

        String rawToken = authorizationHeader.substring(7);

        if (!rawToken.startsWith("agt_")) {
            logger.debug("Authorization header is not an Agent Token.");
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = agentAuthenticator.authenticate(rawToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info(
                "Agent authenticated successfully. AgentId={}",
                currentAgentProvider
                        .currentAgent()
                        .getPublicId()
        );

        filterChain.doFilter(request,response);

    }
}
