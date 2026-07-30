package com.telemtry.telemetryserver.user.infrastructure.security.pat;

import com.telemtry.telemetryserver.user.domain.model.PersonalAccessToken;
import com.telemtry.telemetryserver.user.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class PatPrincipal implements UserDetails {

    private final User owner;
    private final PersonalAccessToken token;

    public PatPrincipal(User owner, PersonalAccessToken token) {
        this.owner = owner;
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return owner.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .toList();
    }

    public User getOwner() {
        return owner;
    }

    public PersonalAccessToken getToken() {
        return token;
    }

    @Override
    public String getPassword() {
        return owner.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return owner.getEmail();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
