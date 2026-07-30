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

    private final PatJpaRepository patJpaRepository;

    public JpaPatRepository(PatJpaRepository patJpaRepository) {
        this.patJpaRepository = patJpaRepository;
    }

    @Override
    public PersonalAccessToken save(PersonalAccessToken token) {
        return patJpaRepository.save(token);
    }

    @Override
    public Optional<PersonalAccessToken> findByTokenHash(String hash) {
        return patJpaRepository.findByTokenHash(hash);
    }

    @Override
    public void delete(PersonalAccessToken token) {

        patJpaRepository.delete(token);

    }

    @Override
    public Optional<PersonalAccessToken> findByPublicId(String tokenId) {
        return patJpaRepository.findByPublicId(tokenId);
    }

    @Override
    public Optional<PersonalAccessToken> findByTokenHashWithOwner(String hash) {
        return patJpaRepository.findByTokenHashWithOwner(hash);
    }


}
