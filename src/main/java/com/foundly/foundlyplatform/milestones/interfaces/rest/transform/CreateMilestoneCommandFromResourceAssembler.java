package com.foundly.foundlyplatform.milestones.interfaces.rest.transform;

import com.foundly.foundlyplatform.milestones.domain.model.commands.CreateMilestoneCommand;
import com.foundly.foundlyplatform.milestones.domain.model.commands.CreateTaskInMilestoneCommand;
import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.ChecklistStep;
import com.foundly.foundlyplatform.milestones.interfaces.rest.resources.CreateMilestoneResource;

import java.util.List;

public final class CreateMilestoneCommandFromResourceAssembler {

    private CreateMilestoneCommandFromResourceAssembler() {}

    public static CreateMilestoneCommand toCommandFromResource(CreateMilestoneResource resource) {
        List<CreateTaskInMilestoneCommand> tasks = null;

        if (resource.tasks() != null) {
            tasks = resource.tasks().stream()
                    .map(t -> {
                        List<ChecklistStep> checklist = t.checklist() == null ? List.of() :
                                t.checklist().stream()
                                        .map(step -> new ChecklistStep(step.description(), step.done()))
                                        .toList();
                        return new CreateTaskInMilestoneCommand(
                                t.title(), t.description(), t.assigneeId(),
                                checklist,
                                t.attachments() != null ? t.attachments() : List.of()
                        );
                    }).toList();
        }

        return new CreateMilestoneCommand(
                resource.projectId(),
                resource.creatorId(),
                resource.title(),
                resource.description(),
                resource.dueDate(),
                resource.tools() != null ? resource.tools() : List.of(),
                resource.generalComment(),
                resource.attachments() != null ? resource.attachments() : List.of(),
                tasks
        );
    }
}
