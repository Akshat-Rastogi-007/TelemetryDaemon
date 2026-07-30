package com.telemtry.telemetryserver.user.infrastructure.security.user;

import com.telemtry.telemetryserver.common.exception.UnauthenticatedException;
import com.telemtry.telemetryserver.user.api.CurrentUserProvider;
import com.telemtry.telemetryserver.user.domain.model.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Configuration("userSecurityCurrentUserProvider")
public class UserSecurityCurrentUserProvider implements CurrentUserProvider {

    @Override
    public User currentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new UnauthenticatedException("Not logged in");
        }

        CurrentUser principal =
                (CurrentUser) authentication.getPrincipal();


        return principal.getUser();
    }
}
