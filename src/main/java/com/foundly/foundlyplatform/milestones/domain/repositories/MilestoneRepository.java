package com.foundly.foundlyplatform.milestones.domain.repositories;

import com.foundly.foundlyplatform.milestones.domain.model.aggregates.Milestone;
import com.foundly.foundlyplatform.milestones.domain.model.entities.MilestoneTask;

import java.util.List;
import java.util.Optional;

public interface MilestoneRepository {
    Milestone save(Milestone milestone);
    Optional<Milestone> findById(String milestoneId);
    List<Milestone> findByProjectId(String projectId);
    void deleteById(String milestoneId);
    Optional<MilestoneTask> findTaskById(String taskId);
    // Returns the milestone that contains the task
    Optional<Milestone> findMilestoneByTaskId(String taskId);
}
