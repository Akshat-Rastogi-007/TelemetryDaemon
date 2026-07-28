package com.telemtry.telemetryserver.user.api.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {

    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime registeredAt;


}
