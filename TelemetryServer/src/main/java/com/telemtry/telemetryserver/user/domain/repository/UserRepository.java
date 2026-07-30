package com.telemtry.telemetryserver.user.domain.repository;

import com.telemtry.telemetryserver.user.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {


    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    void delete(User user);

    public Optional<User> findByIdWithTokens(long id);

}
