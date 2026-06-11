package com.foundly.foundlyplatform.profiles.infrastructure.persistance.jpa.repositories;

import com.foundly.foundlyplatform.profiles.domain.model.aggregates.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {

    Optional<Profile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    boolean existsByUsernameUsername(String username);
}
