package com.foundly.foundlyplatform.milestones.infrastructure.persistence.jpa.assemblers;

import com.foundly.foundlyplatform.milestones.domain.model.aggregates.Milestone;
import com.foundly.foundlyplatform.milestones.domain.model.entities.MilestoneTask;
import com.foundly.foundlyplatform.milestones.infrastructure.persistence.jpa.entities.MilestonePersistenceEntity;
import com.foundly.foundlyplatform.milestones.infrastructure.persistence.jpa.entities.MilestoneTaskPersistenceEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class MilestonePersistenceAssembler {

    private MilestonePersistenceAssembler() {}

    // ── Domain → Persistence ──────────────────────────────────────────────────

    public static MilestonePersistenceEntity toPersistenceFromDomain(Milestone milestone) {
        if (milestone == null) return null;

        var entity = new MilestonePersistenceEntity();
        entity.setId(milestone.getId());
        entity.setProjectId(milestone.getProjectId());
        entity.setCreatorId(milestone.getCreatorId());
        entity.setTitle(milestone.getTitle());
        entity.setDescription(milestone.getDescription());
        entity.setTools(new ArrayList<>(milestone.getTools()));
        entity.setGeneralComment(milestone.getGeneralComment());
        entity.setDueDate(milestone.getDueDate());
        entity.setAttachments(new ArrayList<>(milestone.getAttachments()));
        entity.setStatus(milestone.getStatus());
        entity.setDeliveryUrl(milestone.getDeliveryUrl());
        entity.setDeliveryNotes(milestone.getDeliveryNotes());
        entity.setCreatedAt(milestone.getCreatedAt());
        entity.setUpdatedAt(milestone.getUpdatedAt());

        var taskEntities = milestone.getTasks().stream()
                .map(MilestonePersistenceAssembler::taskToPersistenceFromDomain)
                .collect(Collectors.toList());
        entity.setTasks(taskEntities);

        return entity;
    }

    public static MilestoneTaskPersistenceEntity taskToPersistenceFromDomain(MilestoneTask task) {
        if (task == null) return null;

        var entity = new MilestoneTaskPersistenceEntity();
        entity.setId(task.getId());
        entity.setMilestoneId(task.getMilestoneId());
        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setAssigneeId(task.getAssigneeId());
        entity.setDueDate(task.getDueDate());
        entity.setChecklist(new ArrayList<>(task.getChecklist()));
        entity.setAttachments(new ArrayList<>(task.getAttachments()));
        entity.setStatus(task.getStatus());
        entity.setDeliveryUrl(task.getDeliveryUrl());
        entity.setDeliveryNotes(task.getDeliveryNotes());
        entity.setCreatedAt(task.getCreatedAt());
        entity.setUpdatedAt(task.getUpdatedAt());
        return entity;
    }

    // ── Persistence → Domain ──────────────────────────────────────────────────

    public static Milestone toDomainFromPersistence(MilestonePersistenceEntity entity) {
        if (entity == null) return null;

        var milestone = new Milestone();
        milestone.setId(entity.getId());
        milestone.setProjectId(entity.getProjectId());
        milestone.setCreatorId(entity.getCreatorId());
        milestone.setTitle(entity.getTitle());
        milestone.setDescription(entity.getDescription());
        milestone.setTools(new ArrayList<>(entity.getTools()));
        milestone.setGeneralComment(entity.getGeneralComment());
        milestone.setDueDate(entity.getDueDate());
        milestone.setAttachments(new ArrayList<>(entity.getAttachments()));
        milestone.setStatus(entity.getStatus());
        milestone.setDeliveryUrl(entity.getDeliveryUrl());
        milestone.setDeliveryNotes(entity.getDeliveryNotes());
        milestone.setCreatedAt(entity.getCreatedAt());
        milestone.setUpdatedAt(entity.getUpdatedAt());

        var tasks = entity.getTasks().stream()
                .map(MilestonePersistenceAssembler::taskToDomainFromPersistence)
                .collect(Collectors.toList());
        milestone.setTasks(tasks);

        return milestone;
    }

    public static MilestoneTask taskToDomainFromPersistence(MilestoneTaskPersistenceEntity entity) {
        if (entity == null) return null;

        var task = new MilestoneTask();
        task.setId(entity.getId());
        task.setMilestoneId(entity.getMilestoneId());
        task.setTitle(entity.getTitle());
        task.setDescription(entity.getDescription());
        task.setAssigneeId(entity.getAssigneeId());
        task.setDueDate(entity.getDueDate());
        task.setChecklist(new ArrayList<>(entity.getChecklist()));
        task.setAttachments(new ArrayList<>(entity.getAttachments()));
        task.setStatus(entity.getStatus());
        task.setDeliveryUrl(entity.getDeliveryUrl());
        task.setDeliveryNotes(entity.getDeliveryNotes());
        task.setCreatedAt(entity.getCreatedAt());
        task.setUpdatedAt(entity.getUpdatedAt());
        return task;
    }
}
