package com.telemtry.telemetryserver.user.api.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserResponseDto {

    private String publicId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime registeredAt;
    private List<PersonalAccessTokenResponseDto> personalAccessTokenResponseDtoList;

}
