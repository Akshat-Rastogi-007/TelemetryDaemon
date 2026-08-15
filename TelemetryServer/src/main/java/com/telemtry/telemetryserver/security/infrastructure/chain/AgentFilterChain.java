package com.telemtry.telemetryserver.security.infrastructure.chain;

import com.telemtry.telemetryserver.security.infrastructure.filter.AgentFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class AgentFilterChain {

    private final AgentFilter agentFilter;

    public AgentFilterChain(AgentFilter agentFilter) {
        this.agentFilter = agentFilter;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain agentSecurityChain(HttpSecurity http)
            throws Exception {

        http

                .securityMatcher("/app/agent/telemetry/**","/app/agent/heartbeat/**")

                .authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated()
                )

                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(
                        agentFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();

    }
}
