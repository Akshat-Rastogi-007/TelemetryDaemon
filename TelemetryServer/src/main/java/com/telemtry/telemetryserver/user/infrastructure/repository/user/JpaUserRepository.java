package com.telemtry.telemetryserver.user.infrastructure.repository.user;

import com.telemtry.telemetryserver.user.domain.model.User;
import com.telemtry.telemetryserver.user.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("jpaUserRepository")
public class JpaUserRepository implements UserRepository {

    private final UserJpaRepository repository;

    public JpaUserRepository(UserJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {

        return repository.save(user);

    }

    @Override
    public Optional<User> findById(Long id) {

        return repository.findById(id);

    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {

        return findByEmail(email).isPresent();

    }

    @Override
    public void delete(User user) {
        repository.delete(user);
    }
}
