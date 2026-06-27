package com.foundly.foundlyplatform.milestones.application.queryservices;

import com.foundly.foundlyplatform.milestones.domain.model.aggregates.Milestone;
import com.foundly.foundlyplatform.milestones.domain.model.entities.MilestoneTask;
import com.foundly.foundlyplatform.milestones.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface MilestoneQueryService {
    Optional<Milestone> handle(GetMilestoneByIdQuery query);
    List<Milestone> handle(GetMilestonesByProjectIdQuery query);
    Optional<MilestoneTask> handle(GetTaskByIdQuery query);
    List<MilestoneTask> handle(GetTasksByAssigneeQuery query);
}
