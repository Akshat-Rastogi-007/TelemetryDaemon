package com.telemtry.telemetryserver.user.application.user;

import com.telemtry.telemetryserver.common.exception.InternalResourceCorruptionError;
import com.telemtry.telemetryserver.common.exception.ResourceAlreadyExistsException;
import com.telemtry.telemetryserver.user.api.CurrentUserProvider;
import com.telemtry.telemetryserver.user.api.request.UserRequestDto;
import com.telemtry.telemetryserver.user.api.response.UserResponseDto;
import com.telemtry.telemetryserver.user.application.pat.PersonalAccessTokenService;
import com.telemtry.telemetryserver.user.domain.model.Roles;
import com.telemtry.telemetryserver.user.domain.model.User;
import com.telemtry.telemetryserver.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service()
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PersonalAccessTokenService service;
    private final CurrentUserProvider currentUserProvider;
    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);


    public UserService(
            @Qualifier("jpaUserRepository")
            UserRepository userRepository,
            @Qualifier("passwordEncoder")
            BCryptPasswordEncoder passwordEncoder, PersonalAccessTokenService service,
            @Qualifier("userSecurityCurrentUserProvider")
            CurrentUserProvider currentUserProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.service = service;
        this.currentUserProvider = currentUserProvider;
    }


    @Transactional
    public UserResponseDto registerUser(UserRequestDto dto){
        logger.info("Starting user registration.");

        logger.debug(
                "Checking whether email is already registered."
        );

        if (userRepository.existsByEmail(dto.getEmail())) {

            logger.warn(
                    "User registration failed. Email is already registered."
            );

            throw new ResourceAlreadyExistsException(
                    "Email is already registered."
            );
        }

        User user = mapToUser(dto);

        logger.debug("Encoding user password.");
        String encodedPass = passwordEncoder.encode(dto.getPassword());

        user.getRoles().add(Roles.ROLE_USER);

        user.setPasswordHash(encodedPass);

        User savedUser = userRepository.save(user);

        logger.info(
                "User registered successfully. UserId={}",
                savedUser.getPublicId()
        );

        return mapToUserResponse(savedUser);

    }

    public UserResponseDto updateUser(User user){

        logger.info(
                "Updating user profile. UserId={}",
                user.getPublicId()
        );

        User save = userRepository.save(user);

        logger.info(
                "User profile updated successfully. UserId={}",
                save.getPublicId()
        );
        return mapToUserResponse(save);

    }

    private UserResponseDto mapToUserResponse(User user){

        UserResponseDto responseDto = new UserResponseDto();

        responseDto.setPublicId(user.getPublicId());
        responseDto.setFirstName(user.getFirstName());
        responseDto.setLastName(user.getLastName());
        responseDto.setEmail(user.getEmail());
        responseDto.setRegisteredAt(user.getRegisteredAt());

        responseDto.setPersonalAccessTokenResponseDtoList(service.mapToResponseDto(user.getTokens()));

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

        long id = currentUserProvider.currentUser().getId();

        User currentUser = currentUserProvider.currentUser();

        logger.info(
                "Fetching profile for authenticated user. UserId={}",
                currentUser.getPublicId()
        );


        User user = userRepository.findByIdWithTokens(id).orElseThrow(

                () -> {
                    logger.error(
                            "Authenticated user could not be found in the database. UserId={}",
                            currentUser.getPublicId()
                    );

                    return new InternalResourceCorruptionError(
                            "Your account could not be loaded. Please log in again."
                    );
                }
        );

        logger.info(
                "Profile retrieved successfully. UserId={}",
                user.getPublicId()
        );

        return mapToUserResponse(user);

    }

}
