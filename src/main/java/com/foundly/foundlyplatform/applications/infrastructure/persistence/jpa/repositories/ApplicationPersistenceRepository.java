package com.foundly.foundlyplatform.applications.infrastructure.persistence.jpa.repositories;

import com.foundly.foundlyplatform.applications.infrastructure.persistence.jpa.entities.ApplicationPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for ApplicationPersistenceEntity.
 */
public interface ApplicationPersistenceRepository extends JpaRepository<ApplicationPersistenceEntity, Long> {

    List<ApplicationPersistenceEntity> findByProjectId(Long projectId);

    List<ApplicationPersistenceEntity> findByUserId(Long userId);

    List<ApplicationPersistenceEntity> findByProjectIdAndUserId(Long projectId, Long userId);

    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
}
