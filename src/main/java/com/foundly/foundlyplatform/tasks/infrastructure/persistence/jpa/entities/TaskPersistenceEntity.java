package com.foundly.foundlyplatform.tasks.infrastructure.persistence.jpa.entities;

import com.foundly.foundlyplatform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
import com.foundly.foundlyplatform.tasks.domain.model.valueobjects.TaskStatus;
import com.foundly.foundlyplatform.tasks.infrastructure.persistence.jpa.converters.ChecklistConverter;
import com.foundly.foundlyplatform.tasks.infrastructure.persistence.jpa.converters.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class TaskPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "project_id", nullable = false)
    private String projectId;

    @Column(name = "assignee_id", nullable = false)
    private String assigneeId;

    @Column(name = "creator_id", nullable = false)
    private String creatorId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @Column(name = "due_date", nullable = false)
    private Date dueDate;

    @Convert(converter = ChecklistConverter.class)
    @Column(name = "checklist", columnDefinition = "TEXT")
    private List<Task.ChecklistItem> checklist;

    @Convert(converter = StringListConverter.class)
    @Column(name = "attachments", columnDefinition = "TEXT")
    private List<String> attachments;

    @Convert(converter = StringListConverter.class)
    @Column(name = "tools", columnDefinition = "TEXT")
    private List<String> tools;

    @Column(name = "comment", length = 1000)
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @Column(name = "delivery_url")
    private String deliveryUrl;

    @Column(name = "delivery_notes", length = 1000)
    private String deliveryNotes;
}
