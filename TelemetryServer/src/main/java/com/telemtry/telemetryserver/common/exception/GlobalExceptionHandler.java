package com.telemtry.telemetryserver.common.exception;

import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Map EMPTY_MAP  = Collections.EMPTY_MAP;


    @ExceptionHandler
    public ResponseEntity<ApiResponseDto<?>> resourceNotFoundException(ResourceNotFoundException resourceNotFoundException){

        return new ResponseEntity<>(

                new ApiResponseDto<>(

                        EMPTY_MAP,
                        HttpStatus.NOT_FOUND,
                        "Resource " + resourceNotFoundException.getMessage() + " not found "

                ),
                HttpStatus.NOT_FOUND
        );

    }


}
