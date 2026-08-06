package com.telemtry.telemetryserver.security.infrastructure.chain;

import com.telemtry.telemetryserver.security.infrastructure.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class JwtSecurityFilterChain {

    private final JwtFilter jwtFilter;

    public JwtSecurityFilterChain(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    @Bean
    @Order(4)
    public SecurityFilterChain jwtSecurityChain(HttpSecurity http)
            throws Exception {

        http

                .securityMatcher(
                        "/app/user/pat/**",
                        "/app/user/me/",
                        "/app/telemetry/dashboard/**",
                        "/app/dashboard/agent/**"
                )

                .authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated()
                )

                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();

    }
}
