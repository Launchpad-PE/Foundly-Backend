package com.foundly.foundlyplatform.tasks.infrastructure.persistence.jpa.assemblers;

import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
import com.foundly.foundlyplatform.tasks.infrastructure.persistence.jpa.entities.TaskPersistenceEntity;

public class TaskPersistenceAssembler {

    public static Task toDomainModel(TaskPersistenceEntity entity) {
        var task = new Task();
        task.setId(entity.getId());
        task.setProjectId(entity.getProjectId());
        task.setAssigneeId(entity.getAssigneeId());
        task.setCreatorId(entity.getCreatorId());
        task.setTitle(entity.getTitle());
        task.setDescription(entity.getDescription());
        task.setDueDate(entity.getDueDate());
        task.setChecklist(entity.getChecklist());
        task.setAttachments(entity.getAttachments());
        task.setTools(entity.getTools());
        task.setComment(entity.getComment());
        task.setStatus(entity.getStatus());
        task.setDeliveryUrl(entity.getDeliveryUrl());
        task.setDeliveryNotes(entity.getDeliveryNotes());
        task.setCreatedAt(entity.getCreatedAt());
        task.setUpdatedAt(entity.getUpdatedAt());
        return task;
    }

    public static TaskPersistenceEntity toPersistenceEntity(Task task) {
        var entity = new TaskPersistenceEntity();
        entity.setId(task.getId());
        entity.setProjectId(task.getProjectId());
        entity.setAssigneeId(task.getAssigneeId());
        entity.setCreatorId(task.getCreatorId());
        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setDueDate(task.getDueDate());
        entity.setChecklist(task.getChecklist());
        entity.setAttachments(task.getAttachments());
        entity.setTools(task.getTools());
        entity.setComment(task.getComment());
        entity.setStatus(task.getStatus());
        entity.setDeliveryUrl(task.getDeliveryUrl());
        entity.setDeliveryNotes(task.getDeliveryNotes());
        return entity;
    }
}
