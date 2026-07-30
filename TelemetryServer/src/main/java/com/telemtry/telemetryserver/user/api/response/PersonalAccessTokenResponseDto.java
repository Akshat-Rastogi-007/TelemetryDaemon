package com.telemtry.telemetryserver.user.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PersonalAccessTokenResponseDto {


    private String publicId;

    private String tokenName;

    private LocalDateTime creationDate;

    private LocalDateTime expirationDateAndTime;

    private Boolean revoked;

}
