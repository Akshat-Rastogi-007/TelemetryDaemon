package com.telemtry.telemetryserver.user.infrastructure.repository.pat;

import com.telemtry.telemetryserver.user.domain.model.PersonalAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PatJpaRepository extends JpaRepository<PersonalAccessToken,Long> {

    Optional<PersonalAccessToken> findByTokenHash(String token);

    @Query("""
    SELECT pat
    FROM PersonalAccessToken pat
    LEFT JOIN FETCH pat.owner
    WHERE pat.tokenHash = :tokenHash
    """)
    Optional<PersonalAccessToken> findByTokenHashWithOwner(String tokenHash);

    Optional<PersonalAccessToken> findByPublicId(String tokenId);
}
