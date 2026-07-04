package com.foundly.foundlyplatform.tasks.interfaces.rest.transform;

import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
import com.foundly.foundlyplatform.tasks.domain.model.commands.CreateTaskCommand;
import com.foundly.foundlyplatform.tasks.interfaces.rest.resources.CreateTaskResource;

import java.util.List;

public class CreateTaskCommandFromResourceAssembler {

    public static CreateTaskCommand toCommandFromResource(CreateTaskResource resource) {
        return toCommandFromResource(resource, resource.creatorId());
    }

    /**
     * Builds the command using an explicit creatorId (the authenticated requester),
     * instead of trusting whatever creatorId the client sent in the request body.
     */
    public static CreateTaskCommand toCommandFromResource(CreateTaskResource resource, String creatorId) {
        List<Task.ChecklistItem> checklist = resource.checklist() == null ? List.of() :
                resource.checklist().stream()
                        .map(c -> new Task.ChecklistItem(c.description(), c.done()))
                        .toList();

        return new CreateTaskCommand(
                resource.projectId(),
                resource.assigneeId(),
                creatorId,
                resource.title(),
                resource.description(),
                resource.dueDate(),
                checklist,
                resource.attachments() != null ? resource.attachments() : List.of(),
                resource.tools() != null ? resource.tools() : List.of(),
                resource.comment()
        );
    }
}
