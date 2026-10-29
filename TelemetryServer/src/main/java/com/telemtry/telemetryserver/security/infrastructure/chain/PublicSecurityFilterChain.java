package com.telemtry.telemetryserver.security.infrastructure.chain;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class PublicSecurityFilterChain {

        @Bean
        @Order(1)
        public SecurityFilterChain publicSecurityChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {

            http
                    .cors(cors -> cors.configurationSource(corsConfigurationSource))
                    .securityMatcher(
                            "/app/auth/**",
                            "/app/user/register-user/**"
                    )
                    .authorizeHttpRequests(auth ->
                            auth.anyRequest().permitAll()
                    )
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    );

            return http.build();
        }

}
