package com.telemtry.telemetryserver.common.exception;

import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<String, String> EMPTY_MAP = Collections.emptyMap();


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> resourceNotFoundException(
            ResourceNotFoundException e
    ) {
        e.printStackTrace();


        return buildResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
    }


    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<?>> resourceAlreadyExistsException(
            ResourceAlreadyExistsException e
    ) {


        e.printStackTrace();

        return buildResponse(
                HttpStatus.CONFLICT,
                e.getMessage()
        );
    }


    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ApiResponseDto<?>> tokenInvalidException(
            TokenInvalidException e
    ) {

        e.printStackTrace();

        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                e.getMessage()
        );
    }


    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ApiResponseDto<?>> unauthenticatedException(
            UnauthenticatedException e
    ) {

        e.printStackTrace();

        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                e.getMessage()
        );
    }


    @ExceptionHandler(AgentCredentialGenerationException.class)
    public ResponseEntity<ApiResponseDto<?>> agentCredentialGenerationException(
            AgentCredentialGenerationException e
    ) {

        e.printStackTrace();


        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<?>> exception(Exception e) {

        e.printStackTrace();

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
    }


    private ResponseEntity<ApiResponseDto<?>> buildResponse(
            HttpStatus status,
            String message
    ) {

        return new ResponseEntity<>(
                new ApiResponseDto<>(
                        EMPTY_MAP,
                        status,
                        message
                ),
                status
        );
    }
}