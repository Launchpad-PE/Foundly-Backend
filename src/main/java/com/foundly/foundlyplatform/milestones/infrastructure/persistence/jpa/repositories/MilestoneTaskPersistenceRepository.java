package com.foundly.foundlyplatform.milestones.infrastructure.persistence.jpa.repositories;

import com.foundly.foundlyplatform.milestones.infrastructure.persistence.jpa.entities.MilestoneTaskPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilestoneTaskPersistenceRepository extends JpaRepository<MilestoneTaskPersistenceEntity, String> {
    List<MilestoneTaskPersistenceEntity> findByAssigneeId(String assigneeId);
}
