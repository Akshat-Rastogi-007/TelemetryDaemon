package com.telemtry.telemetryserver.user.infrastructure.repository.user;

import com.telemtry.telemetryserver.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
}
