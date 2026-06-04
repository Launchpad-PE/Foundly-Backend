package com.foundly.foundlyplatform.iam.domain.repositories;

import com.foundly.foundlyplatform.iam.domain.model.aggregates.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * IAM user repository port.
 */
@Repository
public interface UserRepository {
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    User save(User user);

    boolean existsByUsername(String username);
}