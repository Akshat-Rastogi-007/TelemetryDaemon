package com.telemtry.telemetryserver.security.application;

import com.telemtry.telemetryserver.common.exception.UnauthenticatedException;
import com.telemtry.telemetryserver.security.api.request.LoginRequest;
import com.telemtry.telemetryserver.security.api.response.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;



    public AuthenticationService(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse login(LoginRequest request){


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        System.out.println(authenticate.isAuthenticated());

        if (!authenticate.isAuthenticated()){

            throw new UnauthenticatedException("Please check your id or password");

        }

        String token = jwtService.generateToken((UserDetails) authenticate.getPrincipal());


        return new LoginResponse(token);

    }


}
