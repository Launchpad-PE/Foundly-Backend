package com.foundly.foundlyplatform.iam.domain.repositories;

import com.foundly.foundlyplatform.iam.domain.model.entities.Role;
import com.foundly.foundlyplatform.iam.domain.model.valueobjects.Roles;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * IAM role repository port.
 */
public interface RoleRepository {
    Optional<Role> findByName(Roles name);

    List<Role> findAll();

    Role save(Role role);

    boolean existsByName(Roles name);
}