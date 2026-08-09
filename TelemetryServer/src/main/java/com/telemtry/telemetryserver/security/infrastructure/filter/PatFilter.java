package com.telemtry.telemetryserver.security.infrastructure.filter;


import com.telemtry.telemetryserver.user.api.PatAuthenticator;
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
public class PatFilter extends OncePerRequestFilter {

    private final PatAuthenticator patAuthenticationService;
    private final Logger logger = LoggerFactory.getLogger(PatFilter.class);

    public PatFilter(
            @Qualifier("patAuthenticationService")
            PatAuthenticator patAuthenticationService) {
        this.patAuthenticationService = patAuthenticationService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        logger.debug(
                "Processing PAT authentication for {} {}",
                request.getMethod(),
                request.getRequestURI()
        );

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.debug("Authorization header is not a PAT.");
            filterChain.doFilter(request, response);
            return;
        }

        String rawToken = authorizationHeader.substring(7);


        if (!rawToken.startsWith("pat_")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication =
                patAuthenticationService.authenticate(rawToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("PAT authenticated successfully.");
        
        filterChain.doFilter(request,response);
    }


}

