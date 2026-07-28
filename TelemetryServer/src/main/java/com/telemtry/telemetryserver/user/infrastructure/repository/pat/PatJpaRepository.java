package com.telemtry.telemetryserver.user.infrastructure.repository.pat;

import com.telemtry.telemetryserver.user.domain.model.PersonalAccessToken;
import com.telemtry.telemetryserver.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatJpaRepository extends JpaRepository<PersonalAccessToken,Long> {

}
