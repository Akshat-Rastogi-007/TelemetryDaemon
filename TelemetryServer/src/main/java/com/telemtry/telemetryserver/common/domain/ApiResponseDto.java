package com.telemtry.telemetryserver.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
public class ApiResponseDto<T> {

    private T data;
    private HttpStatusCode statusCode;
    private String message;

}
