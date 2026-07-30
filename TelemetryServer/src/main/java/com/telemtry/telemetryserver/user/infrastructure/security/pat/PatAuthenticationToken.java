package com.telemtry.telemetryserver.user.infrastructure.security.pat;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class PatAuthenticationToken extends AbstractAuthenticationToken {

    private final PatPrincipal principal;

    public PatAuthenticationToken(PatPrincipal principal) {
        super(principal.getAuthorities());
        this.principal = principal;
        setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {

        return principal;
    }
}
