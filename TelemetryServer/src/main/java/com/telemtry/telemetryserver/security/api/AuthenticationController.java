package com.telemtry.telemetryserver.security.api;

import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import com.telemtry.telemetryserver.security.api.request.LoginRequest;
import com.telemtry.telemetryserver.security.api.response.LoginResponse;
import com.telemtry.telemetryserver.security.application.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/auth")
public class AuthenticationController {


    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<?>> login(@RequestBody LoginRequest loginRequest){

        System.out.println("HIII");

        LoginResponse response = authenticationService.login(loginRequest);

        return new ResponseEntity<>(

                new ApiResponseDto<>(
                        response,
                        HttpStatus.OK
                        ,"Successfully Logged in"
                )
                ,HttpStatus.OK
        );


    }
}
