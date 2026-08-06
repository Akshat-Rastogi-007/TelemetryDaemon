package com.telemtry.telemetryserver.user.api.controller;

import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import com.telemtry.telemetryserver.user.api.request.PersonalAccessTokenRequestDto;
import com.telemtry.telemetryserver.user.application.pat.PersonalAccessTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/app/user/pat/")
public class PatController {


    private final PersonalAccessTokenService service;

    public PatController(PersonalAccessTokenService service) {
        this.service = service;
    }


    @PostMapping("/register-pat/")
    public ResponseEntity<ApiResponseDto<?>> registerPat(@RequestBody PersonalAccessTokenRequestDto dto){


        String token = service.createToken(dto);

        return new ResponseEntity<>(

                new ApiResponseDto<>(
                        token,
                        HttpStatus.CREATED,
                        "Pat Created Successfully"
                ),
                HttpStatus.CREATED

        );

    }


    @PutMapping("/revoke-pat/")
    public ResponseEntity<ApiResponseDto<?>> revokePat(String tokenId){

        service.revokePat(tokenId);

        return new ResponseEntity<>(

                new ApiResponseDto<>(

                        Collections.EMPTY_MAP,HttpStatus.OK,"TOKEN REVOKED"
                )
                ,HttpStatus.OK
        );

    }
}
