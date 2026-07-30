package com.telemtry.telemetryserver.security.application.filter;


import com.telemtry.telemetryserver.user.api.PatAuthenticator;
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
public class PatFilter extends OncePerRequestFilter {

    private final PatAuthenticator patAuthenticationService;

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

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
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

        
        filterChain.doFilter(request,response);
    }


}

