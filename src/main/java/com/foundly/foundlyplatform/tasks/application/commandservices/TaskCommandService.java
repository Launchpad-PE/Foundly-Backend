package com.foundly.foundlyplatform.tasks.application.commandservices;

import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
import com.foundly.foundlyplatform.tasks.domain.model.commands.CreateTaskCommand;
import com.foundly.foundlyplatform.tasks.domain.model.commands.DeleteTaskCommand;
import com.foundly.foundlyplatform.tasks.domain.model.commands.PatchTaskCommand;

import java.util.Optional;

public interface TaskCommandService {
    Optional<Task> handle(CreateTaskCommand command);
    Optional<Task> handle(PatchTaskCommand command);
    void handle(DeleteTaskCommand command);
}
