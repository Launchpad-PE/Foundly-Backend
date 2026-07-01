package com.foundly.foundlyplatform.applications.infrastructure.persistence.jpa.repositories;

import com.foundly.foundlyplatform.applications.infrastructure.persistence.jpa.entities.ApplicationPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for ApplicationPersistenceEntity.
 */
public interface ApplicationPersistenceRepository extends JpaRepository<ApplicationPersistenceEntity, String> {

    List<ApplicationPersistenceEntity> findByProjectId(String projectId);  // ← Long → String

    List<ApplicationPersistenceEntity> findByUserId(String userId);  // ← Long → String

    List<ApplicationPersistenceEntity> findByProjectIdAndUserId(String projectId, String userId);  // ← Long → String

    boolean existsByProjectIdAndUserId(String projectId, String userId);  // ← Long → String
}
