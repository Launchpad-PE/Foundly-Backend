package com.foundly.foundlyplatform.tasks.interfaces.rest.transform;

import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
import com.foundly.foundlyplatform.tasks.domain.model.commands.PatchTaskCommand;
import com.foundly.foundlyplatform.tasks.interfaces.rest.resources.PatchTaskResource;

import java.util.List;

public class PatchTaskCommandFromResourceAssembler {

    public static PatchTaskCommand toCommandFromResource(Long id, PatchTaskResource resource) {
        List<Task.ChecklistItem> checklist = resource.checklist() == null ? null :
                resource.checklist().stream()
                        .map(c -> new Task.ChecklistItem(c.description(), c.done()))
                        .toList();

        return new PatchTaskCommand(
                id,
                resource.title(),
                resource.description(),
                resource.dueDate(),
                checklist,
                resource.attachments(),
                resource.tools(),
                resource.comment(),
                resource.status(),
                resource.deliveryUrl(),
                resource.deliveryNotes()
        );
    }
}
