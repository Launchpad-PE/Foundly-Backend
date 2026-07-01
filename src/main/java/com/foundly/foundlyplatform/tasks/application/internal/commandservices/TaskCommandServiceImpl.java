package com.foundly.foundlyplatform.tasks.application.internal.commandservices;

import com.foundly.foundlyplatform.tasks.application.commandservices.TaskCommandService;
import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
import com.foundly.foundlyplatform.tasks.domain.model.commands.CreateTaskCommand;
import com.foundly.foundlyplatform.tasks.domain.model.commands.DeleteTaskCommand;
import com.foundly.foundlyplatform.tasks.domain.model.commands.PatchTaskCommand;
import com.foundly.foundlyplatform.tasks.domain.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskCommandServiceImpl implements TaskCommandService {

    private final TaskRepository taskRepository;

    public TaskCommandServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Optional<Task> handle(CreateTaskCommand command) {
        var task = new Task(
                command.projectId(),
                command.assigneeId(),
                command.creatorId(),
                command.title(),
                command.description(),
                command.dueDate(),
                command.checklist(),
                command.attachments(),
                command.tools(),
                command.comment()
        );
        return Optional.of(taskRepository.save(task));
    }

    @Override
    public Optional<Task> handle(PatchTaskCommand command) {
        return taskRepository.findById(command.id()).map(existing -> {
            if (command.title()        != null) existing.setTitle(command.title());
            if (command.description()  != null) existing.setDescription(command.description());
            if (command.dueDate()      != null) existing.setDueDate(command.dueDate());
            if (command.checklist()    != null) existing.setChecklist(command.checklist());
            if (command.attachments()  != null) existing.setAttachments(command.attachments());
            if (command.tools()        != null) existing.setTools(command.tools());
            if (command.comment()      != null) existing.setComment(command.comment());
            if (command.status()       != null) existing.setStatus(command.status());
            if (command.deliveryUrl()  != null) existing.setDeliveryUrl(command.deliveryUrl());
            if (command.deliveryNotes() != null) existing.setDeliveryNotes(command.deliveryNotes());
            return taskRepository.save(existing);
        });
    }

    @Override
    public void handle(DeleteTaskCommand command) {
        taskRepository.deleteById(command.id());
    }
}
