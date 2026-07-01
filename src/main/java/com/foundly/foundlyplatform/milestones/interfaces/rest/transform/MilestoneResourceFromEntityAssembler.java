package com.foundly.foundlyplatform.milestones.interfaces.rest.transform;

import com.foundly.foundlyplatform.milestones.domain.model.aggregates.Milestone;
import com.foundly.foundlyplatform.milestones.domain.model.entities.MilestoneTask;
import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.ChecklistStep;
import com.foundly.foundlyplatform.milestones.interfaces.rest.resources.ChecklistStepResource;
import com.foundly.foundlyplatform.milestones.interfaces.rest.resources.MilestoneResource;
import com.foundly.foundlyplatform.milestones.interfaces.rest.resources.MilestoneTaskResource;

import java.util.List;

public final class MilestoneResourceFromEntityAssembler {

    private MilestoneResourceFromEntityAssembler() {}

    public static MilestoneResource toResourceFromEntity(Milestone milestone) {
        var tasks = milestone.getTasks().stream()
                .map(MilestoneResourceFromEntityAssembler::toTaskResourceFromEntity)
                .toList();

        return new MilestoneResource(
                milestone.getId(),
                milestone.getProjectId(),
                milestone.getCreatorId(),
                milestone.getTitle(),
                milestone.getDescription(),
                List.copyOf(milestone.getTools()),
                milestone.getGeneralComment(),
                milestone.getDueDate(),
                List.copyOf(milestone.getAttachments()),
                tasks,
                milestone.getStatus(),
                milestone.getDeliveryUrl(),
                milestone.getDeliveryNotes(),
                milestone.getTasksCompletionPercentage(),
                milestone.getCreatedAt(),
                milestone.getUpdatedAt()
        );
    }

    public static MilestoneTaskResource toTaskResourceFromEntity(MilestoneTask task) {
        var checklist = task.getChecklist().stream()
                .map(step -> new ChecklistStepResource(step.getDescription(), step.isDone()))
                .toList();

        return new MilestoneTaskResource(
                task.getId(),
                task.getMilestoneId(),
                task.getTitle(),
                task.getDescription(),
                task.getAssigneeId(),
                task.getDueDate(),
                checklist,
                List.copyOf(task.getAttachments()),
                task.getStatus(),
                task.getDeliveryUrl(),
                task.getDeliveryNotes(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
