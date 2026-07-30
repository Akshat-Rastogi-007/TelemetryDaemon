package com.telemtry.telemetryserver.user.infrastructure.security.pat;

import com.telemtry.telemetryserver.common.exception.UnauthenticatedException;
import com.telemtry.telemetryserver.user.api.PatAuthenticator;
import com.telemtry.telemetryserver.user.application.pat.PersonalAccessTokenService;
import com.telemtry.telemetryserver.user.domain.model.PersonalAccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("patAuthenticationService")
public class PatAuthenticationService implements PatAuthenticator {

    private final PersonalAccessTokenService service;

    public PatAuthenticationService(PersonalAccessTokenService service) {
        this.service = service;
    }


    @Override
    public Authentication authenticate(String rawPat) {

        Optional<PersonalAccessToken> byTokenHash = service.findByTokenHashWithOwner(rawPat);

        if (byTokenHash.isEmpty())
            throw new UnauthenticatedException("Kindly check your PAT again");

        PersonalAccessToken personalAccessToken = byTokenHash.get();

        boolean expiredOrRevoked = service.isExpiredOrRevoked(personalAccessToken);

        if (expiredOrRevoked){

            throw new UnauthenticatedException("Pat is expired or revoked, kindly check again");

        }

        PatPrincipal patPrincipal = new PatPrincipal(personalAccessToken.getOwner(), personalAccessToken);

        return new PatAuthenticationToken(
                patPrincipal
        );
    }
}
