package com.foundly.foundlyplatform.milestones.application.internal.commandservices;

import com.foundly.foundlyplatform.milestones.application.commandservices.MilestoneCommandService;
import com.foundly.foundlyplatform.milestones.domain.model.aggregates.Milestone;
import com.foundly.foundlyplatform.milestones.domain.model.commands.*;
import com.foundly.foundlyplatform.milestones.domain.model.entities.MilestoneTask;
import com.foundly.foundlyplatform.milestones.domain.repositories.MilestoneRepository;
import com.foundly.foundlyplatform.shared.application.result.ApplicationError;
import com.foundly.foundlyplatform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MilestoneCommandServiceImpl implements MilestoneCommandService {

    private final MilestoneRepository milestoneRepository;

    public MilestoneCommandServiceImpl(MilestoneRepository milestoneRepository) {
        this.milestoneRepository = milestoneRepository;
    }

    @Override
    public Result<Milestone, ApplicationError> handle(CreateMilestoneCommand command) {
        if (command.title() == null || command.title().isBlank()) {
            return Result.failure(ApplicationError.validationError("title", "Title must not be empty"));
        }
        if (command.projectId() == null || command.projectId().isBlank()) {
            return Result.failure(ApplicationError.validationError("projectId", "Project ID is required"));
        }
        if (command.creatorId() == null || command.creatorId().isBlank()) {
            return Result.failure(ApplicationError.validationError("creatorId", "Creator ID is required"));
        }
        if (command.dueDate() == null) {
            return Result.failure(ApplicationError.validationError("dueDate", "Due date is required"));
        }

        var milestone = new Milestone(
                command.projectId(),
                command.creatorId(),
                command.title(),
                command.description(),
                command.dueDate(),
                command.tools(),
                command.generalComment(),
                command.attachments()
        );

        // Add initial tasks if provided
        if (command.tasks() != null) {
            for (var taskCmd : command.tasks()) {
                var task = milestone.addTask(taskCmd.title(), taskCmd.description(), taskCmd.assigneeId());
                if (taskCmd.checklist() != null) task.setChecklist(taskCmd.checklist());
                if (taskCmd.attachments() != null) task.setAttachments(taskCmd.attachments());
            }
        }

        milestone.recalculateStatus();
        var saved = milestoneRepository.save(milestone);
        return Result.success(saved);
    }

    @Override
    public Result<Milestone, ApplicationError> handle(UpdateMilestoneCommand command) {
        var milestoneOpt = milestoneRepository.findById(command.milestoneId());
        if (milestoneOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("milestone", "Milestone not found: " + command.milestoneId()));
        }

        var milestone = milestoneOpt.get();
        milestone.updateInfo(
                command.title(),
                command.description(),
                command.tools(),
                command.generalComment(),
                command.attachments()
        );

        if (command.dueDate() != null) {
            milestone.reschedule(command.dueDate());
        }

        var saved = milestoneRepository.save(milestone);
        return Result.success(saved);
    }

    @Override
    public Result<Void, ApplicationError> handle(DeleteMilestoneCommand command) {
        if (milestoneRepository.findById(command.milestoneId()).isEmpty()) {
            return Result.failure(ApplicationError.notFound("milestone", "Milestone not found: " + command.milestoneId()));
        }
        milestoneRepository.deleteById(command.milestoneId());
        return Result.success(null);
    }

    @Override
    public Result<Milestone, ApplicationError> handle(RescheduleMilestoneCommand command) {
        var milestoneOpt = milestoneRepository.findById(command.milestoneId());
        if (milestoneOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("milestone", "Milestone not found"));
        }

        var milestone = milestoneOpt.get();
        try {
            milestone.reschedule(command.newDueDate());
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.validationError("status", e.getMessage()));
        }

        var saved = milestoneRepository.save(milestone);
        return Result.success(saved);
    }

    @Override
    public Result<MilestoneTask, ApplicationError> handle(AddTaskToMilestoneCommand command) {
        var milestoneOpt = milestoneRepository.findById(command.milestoneId());
        if (milestoneOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("milestone", "Milestone not found"));
        }

        var milestone = milestoneOpt.get();
        var task = milestone.addTask(command.title(), command.description(), command.assigneeId());
        if (command.checklist() != null) task.setChecklist(command.checklist());
        if (command.attachments() != null) task.setAttachments(command.attachments());

        milestoneRepository.save(milestone);
        return Result.success(task);
    }

    @Override
    public Result<MilestoneTask, ApplicationError> handle(UpdateTaskStatusCommand command) {
        var milestoneOpt = milestoneRepository.findMilestoneByTaskId(command.taskId());
        if (milestoneOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("task", "Task not found: " + command.taskId()));
        }

        var milestone = milestoneOpt.get();
        milestone.updateTaskStatus(command.taskId(), command.status());

        var saved = milestoneRepository.save(milestone);
        var updatedTask = saved.getTasks().stream()
                .filter(t -> t.getId().equals(command.taskId()))
                .findFirst()
                .orElseThrow();
        return Result.success(updatedTask);
    }

    @Override
    public Result<MilestoneTask, ApplicationError> handle(CompleteTaskCommand command) {
        var milestoneOpt = milestoneRepository.findMilestoneByTaskId(command.taskId());
        if (milestoneOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("task", "Task not found: " + command.taskId()));
        }

        var milestone = milestoneOpt.get();
        milestone.completeTask(command.taskId(), command.deliveryUrl(), command.deliveryNotes());

        var saved = milestoneRepository.save(milestone);
        var completedTask = saved.getTasks().stream()
                .filter(t -> t.getId().equals(command.taskId()))
                .findFirst()
                .orElseThrow();
        return Result.success(completedTask);
    }

    @Override
    public Result<Void, ApplicationError> handle(DeleteTaskCommand command) {
        var milestoneOpt = milestoneRepository.findMilestoneByTaskId(command.taskId());
        if (milestoneOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("task", "Task not found: " + command.taskId()));
        }

        var milestone = milestoneOpt.get();
        milestone.removeTask(command.taskId());
        milestoneRepository.save(milestone);
        return Result.success(null);
    }
}
