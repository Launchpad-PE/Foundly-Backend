package com.foundly.foundlyplatform.tasks.interfaces.rest.transform;

import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
import com.foundly.foundlyplatform.tasks.interfaces.rest.resources.ChecklistItemResource;
import com.foundly.foundlyplatform.tasks.interfaces.rest.resources.TaskResource;

import java.util.List;

public class TaskResourceFromEntityAssembler {

    public static TaskResource toResourceFromEntity(Task task) {
        List<ChecklistItemResource> checklist = task.getChecklist() == null ? List.of() :
                task.getChecklist().stream()
                        .map(c -> new ChecklistItemResource(c.description(), c.done()))
                        .toList();

        return new TaskResource(
                task.getId(),
                task.getProjectId(),
                task.getAssigneeId(),
                task.getCreatorId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                checklist,
                task.getAttachments() != null ? task.getAttachments() : List.of(),
                task.getTools() != null ? task.getTools() : List.of(),
                task.getComment(),
                task.getStatus(),
                task.getDeliveryUrl(),
                task.getDeliveryNotes(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
