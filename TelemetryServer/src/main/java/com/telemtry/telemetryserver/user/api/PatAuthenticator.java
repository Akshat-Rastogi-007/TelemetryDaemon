package com.telemtry.telemetryserver.user.api;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public interface PatAuthenticator {

    Authentication authenticate(String rawPat);

}
