package com.telemtry.telemetryserver.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotNull
    private String firstName;
    private String lastName;
    @Email
    @NotNull
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#^()_+=-])[A-Za-z\\d@$!%*?&.#^()_+=-]{8,64}$",
            message = "Password must contain uppercase, lowercase, digit and special character."
    )
    private String password;

}
