package com.telemtry.telemetryserver.user.api.controller;

import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import com.telemtry.telemetryserver.user.api.request.PersonalAccessTokenRequestDto;
import com.telemtry.telemetryserver.user.application.pat.PersonalAccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/app/user/pat/")
public class PatController {


    private static final Logger logger =
            LoggerFactory.getLogger(PatController.class);
    private final PersonalAccessTokenService service;

    public PatController(PersonalAccessTokenService service) {
        this.service = service;
    }


    @PostMapping("/register-pat/")
    public ResponseEntity<ApiResponseDto<?>> registerPat(@RequestBody PersonalAccessTokenRequestDto dto){


        logger.info("Received request to create a new Personal Access Token.");

        String token = service.createToken(dto);

        logger.info("Personal Access Token created successfully.");

        return new ResponseEntity<>(
                new ApiResponseDto<>(
                        token,
                        HttpStatus.CREATED,
                        "PAT Created Successfully"
                ),
                HttpStatus.CREATED
        );
    }




    @PutMapping("/revoke-pat/")
    public ResponseEntity<ApiResponseDto<?>> revokePat(@RequestParam String tokenId){

        logger.warn("Revoking Personal Access Token. tokenId={}", tokenId);

        service.revokePat(tokenId);

        logger.info("Personal Access Token revoked successfully. tokenId={}", tokenId);

        
        return new ResponseEntity<>(

                new ApiResponseDto<>(

                        Collections.EMPTY_MAP,HttpStatus.OK,"TOKEN REVOKED"
                )
                ,HttpStatus.OK
        );

    }
}
