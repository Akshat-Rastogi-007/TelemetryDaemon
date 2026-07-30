package com.telemtry.telemetryserver.user.infrastructure.security.pat;

import com.telemtry.telemetryserver.user.api.CurrentUserProvider;
import com.telemtry.telemetryserver.user.domain.model.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration("patSecurityCurrentUserProvider")
public class PatSecurityCurrentUserProvider implements CurrentUserProvider {


    @Override
    public User currentUser() {

        PatPrincipal principal = (PatPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return principal.getOwner();

    }
}
