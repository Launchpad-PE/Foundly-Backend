package com.foundly.foundlyplatform.milestones.application.commandservices;

import com.foundly.foundlyplatform.milestones.domain.model.aggregates.Milestone;
import com.foundly.foundlyplatform.milestones.domain.model.commands.*;
import com.foundly.foundlyplatform.milestones.domain.model.entities.MilestoneTask;
import com.foundly.foundlyplatform.shared.application.result.ApplicationError;
import com.foundly.foundlyplatform.shared.application.result.Result;

public interface MilestoneCommandService {
    Result<Milestone, ApplicationError> handle(CreateMilestoneCommand command);
    Result<Milestone, ApplicationError> handle(UpdateMilestoneCommand command);
    Result<Void, ApplicationError> handle(DeleteMilestoneCommand command);
    Result<Milestone, ApplicationError> handle(RescheduleMilestoneCommand command);
    Result<MilestoneTask, ApplicationError> handle(AddTaskToMilestoneCommand command);
    Result<MilestoneTask, ApplicationError> handle(UpdateTaskStatusCommand command);
    Result<MilestoneTask, ApplicationError> handle(CompleteTaskCommand command);
    Result<Void, ApplicationError> handle(DeleteTaskCommand command);
}
