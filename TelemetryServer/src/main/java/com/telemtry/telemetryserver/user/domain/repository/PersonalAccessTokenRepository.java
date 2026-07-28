package com.telemtry.telemetryserver.user.domain.repository;

import com.telemtry.telemetryserver.user.domain.model.PersonalAccessToken;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalAccessTokenRepository {

    PersonalAccessToken save(PersonalAccessToken token);

    Optional<PersonalAccessToken> findByTokenHash(String hash);

    void delete(PersonalAccessToken token);
}
