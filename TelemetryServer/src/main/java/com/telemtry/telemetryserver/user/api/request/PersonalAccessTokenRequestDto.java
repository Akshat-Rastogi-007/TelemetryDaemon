package com.telemtry.telemetryserver.user.api.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PersonalAccessTokenRequestDto {

    private String tokenName;
    private LocalDateTime expirationDate;

}
