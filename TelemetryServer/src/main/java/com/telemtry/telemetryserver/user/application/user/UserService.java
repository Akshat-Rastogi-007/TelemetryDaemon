package com.telemtry.telemetryserver.user.application.user;

import com.telemtry.telemetryserver.common.exception.ResourceAlreadyExistsException;
import com.telemtry.telemetryserver.common.exception.TokenInvalidException;
import com.telemtry.telemetryserver.common.exception.UnauthenticatedException;
import com.telemtry.telemetryserver.user.api.request.UserRequestDto;
import com.telemtry.telemetryserver.user.api.response.UserResponseDto;
import com.telemtry.telemetryserver.user.domain.model.Roles;
import com.telemtry.telemetryserver.user.domain.model.User;
import com.telemtry.telemetryserver.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service()
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(
            @Qualifier("jpaUserRepository")
            UserRepository userRepository,
            @Qualifier("passwordEncoder")
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public UserResponseDto registerUser(UserRequestDto dto){

        if( userRepository.existsByEmail(dto.getEmail())){

            throw new ResourceAlreadyExistsException("Mail already registered with another user");

        }

        User user = mapToUser(dto);

        String encodedPass = passwordEncoder.encode(dto.getPassword());

        user.getRoles().add(Roles.ROLE_USER);

        user.setPasswordHash(encodedPass);

        User savedUser = userRepository.save(user);


        return mapToUserResponse(savedUser);

    }

    private UserResponseDto mapToUserResponse(User user){

        UserResponseDto responseDto = new UserResponseDto();

        responseDto.setId(user.getId());
        responseDto.setFirstName(user.getFirstName());
        responseDto.setLastName(user.getLastName());
        responseDto.setEmail(user.getEmail());
        responseDto.setRegisteredAt(user.getRegisteredAt());


        return responseDto;
    }

    private User mapToUser(UserRequestDto dto) {

        User user = new User();

        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAccountStatus(AccountStatus.ENABLED);

        return user;

    }

    public Optional<User> findUserByEmail(String email) {

        return userRepository.findByEmail(email);

    }


    public UserResponseDto getCurrentUser(){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new UnauthenticatedException("Not logged in");
        }

        UserDetails principal =
                (UserDetails) authentication.getPrincipal();


        User user = findUserByEmail(principal.getUsername())
                .orElseThrow(() ->
                        new TokenInvalidException(
                                "Token is not valid, Kindly Login again"
                        )
                );


        return mapToUserResponse(user);
    }

}
