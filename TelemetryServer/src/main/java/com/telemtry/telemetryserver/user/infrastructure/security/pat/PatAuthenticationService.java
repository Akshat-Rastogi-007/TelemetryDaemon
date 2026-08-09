package com.telemtry.telemetryserver.user.infrastructure.security.pat;

import com.telemtry.telemetryserver.common.exception.UnauthenticatedException;
import com.telemtry.telemetryserver.user.api.PatAuthenticator;
import com.telemtry.telemetryserver.user.application.pat.PersonalAccessTokenService;
import com.telemtry.telemetryserver.user.domain.model.PersonalAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("patAuthenticationService")
public class PatAuthenticationService implements PatAuthenticator {

    private final Logger logger =
            LoggerFactory
                    .getLogger(PatAuthenticationService.class);
    private final PersonalAccessTokenService service;

    public PatAuthenticationService(PersonalAccessTokenService service) {
        this.service = service;
    }


    @Override
    public Authentication authenticate(String rawPat) {
        logger.debug("Authenticating Personal Access Token.");

        Optional<PersonalAccessToken> optionalPat =
                service.findByTokenHashWithOwner(rawPat);

        if (optionalPat.isEmpty()) {

            logger.warn("PAT authentication failed. Token not found.");

            throw new UnauthenticatedException(
                    "Invalid Personal Access Token."
            );
        }

        PersonalAccessToken personalAccessToken = optionalPat.get();

        if (service.isExpiredOrRevoked(personalAccessToken)) {

            logger.warn(
                    "PAT authentication failed. TokenId={} is expired or revoked.",
                    personalAccessToken.getPublicId()
            );

            throw new UnauthenticatedException(
                    "Personal Access Token has expired or has been revoked."
            );
        }

        logger.info(
                "PAT authenticated successfully. TokenId={}",
                personalAccessToken.getPublicId()
        );

        PatPrincipal patPrincipal =
                new PatPrincipal(
                        personalAccessToken.getOwner(),
                        personalAccessToken
                );

        return new PatAuthenticationToken(patPrincipal);
    }
}

