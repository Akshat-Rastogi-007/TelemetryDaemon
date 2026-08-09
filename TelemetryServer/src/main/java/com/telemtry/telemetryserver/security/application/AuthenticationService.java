package com.telemtry.telemetryserver.security.application;

import com.telemtry.telemetryserver.common.exception.UnauthenticatedException;
import com.telemtry.telemetryserver.security.api.request.LoginRequest;
import com.telemtry.telemetryserver.security.api.response.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthenticationService.class);
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationService(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse login(LoginRequest request) {

        logger.info("Authenticating user.");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                );

        try {

            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            logger.info(
                    "User authenticated successfully. username={}",
                    userDetails.getUsername()
            );

            String token = jwtService.generateToken(userDetails);

            return new LoginResponse(token);

        } catch (AuthenticationException ex) {

            logger.warn(
                    "Authentication failed for email={}",
                    request.getEmail()
            );

            throw new UnauthenticatedException(
                    "Invalid email or password."
            );
        }
    }
}



