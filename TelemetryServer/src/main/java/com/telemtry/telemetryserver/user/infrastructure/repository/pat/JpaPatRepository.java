package com.telemtry.telemetryserver.user.infrastructure.repository.pat;

import com.telemtry.telemetryserver.user.domain.model.PersonalAccessToken;
import com.telemtry.telemetryserver.user.domain.model.User;
import com.telemtry.telemetryserver.user.domain.repository.PersonalAccessTokenRepository;
import com.telemtry.telemetryserver.user.domain.repository.UserRepository;
import com.telemtry.telemetryserver.user.infrastructure.repository.user.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("jpaPatRepository")
public class JpaPatRepository implements PersonalAccessTokenRepository {

    @Override
    public PersonalAccessToken save(PersonalAccessToken token) {
        return null;
    }

    @Override
    public Optional<PersonalAccessToken> findByTokenHash(String hash) {
        return Optional.empty();
    }

    @Override
    public void delete(PersonalAccessToken token) {

    }
}
