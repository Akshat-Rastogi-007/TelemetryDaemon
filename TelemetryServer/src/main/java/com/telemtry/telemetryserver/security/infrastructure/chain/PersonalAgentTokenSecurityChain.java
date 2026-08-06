package com.telemtry.telemetryserver.security.infrastructure.chain;

import com.telemtry.telemetryserver.security.infrastructure.filter.PatFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class PersonalAgentTokenSecurityChain {

    private final PatFilter patFilter;

    public PersonalAgentTokenSecurityChain(PatFilter patFilter) {
        this.patFilter = patFilter;
    }

    @Bean
    @Order(3)
    public SecurityFilterChain patSecurityChain(HttpSecurity http)
            throws Exception {

        http

                .securityMatcher("/app/agent/**")

                .authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated()
                )

                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(
                        patFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();

    }
}
