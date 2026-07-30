package com.telemtry.telemetryserver.user.api;

import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import com.telemtry.telemetryserver.user.api.request.UserRequestDto;
import com.telemtry.telemetryserver.user.api.response.UserResponseDto;
import com.telemtry.telemetryserver.user.application.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/user/")
public class UserController {


    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register-user/")
    public ResponseEntity<ApiResponseDto<?>> registerUser(@RequestBody @Valid UserRequestDto requestDto){

        UserResponseDto responseDto = userService.registerUser(requestDto);

        return new ResponseEntity<>(

                new ApiResponseDto<>(
                        responseDto, HttpStatus.CREATED, "Account Created Successfully"
                )
                , HttpStatus.CREATED
        );

    }

    @GetMapping("/me/")
    public ResponseEntity<ApiResponseDto<?>> getCurrentUser(){

        UserResponseDto currentUser = userService.getCurrentUser();

        System.out.println(currentUser);

        return new ResponseEntity<>(

                new ApiResponseDto<>(
                        currentUser, HttpStatus.OK, "Account Retrieved Successfully"
                )
                , HttpStatus.OK
        );


    }
}
