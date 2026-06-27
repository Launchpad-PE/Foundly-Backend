package com.foundly.foundlyplatform.milestones.infrastructure.persistence.jpa.repositories;

import com.foundly.foundlyplatform.milestones.infrastructure.persistence.jpa.entities.MilestonePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilestonePersistenceRepository extends JpaRepository<MilestonePersistenceEntity, String> {
    List<MilestonePersistenceEntity> findByProjectId(String projectId);
}
