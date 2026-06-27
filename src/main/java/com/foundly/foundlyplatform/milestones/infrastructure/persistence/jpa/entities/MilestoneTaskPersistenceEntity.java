package com.foundly.foundlyplatform.milestones.infrastructure.persistence.jpa.entities;

import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.ChecklistStep;
import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.MilestoneTaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "milestone_tasks")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MilestoneTaskPersistenceEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "milestone_id", nullable = false, length = 36)
    private String milestoneId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "assignee_id", nullable = false, length = 100)
    private String assigneeId;

    @Column(name = "due_date")
    private Date dueDate;

    @ElementCollection
    @CollectionTable(name = "milestone_task_checklist", joinColumns = @JoinColumn(name = "task_id"))
    private List<ChecklistStep> checklist = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "milestone_task_attachments", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "attachment_url", length = 500)
    private List<String> attachments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MilestoneTaskStatus status = MilestoneTaskStatus.PENDING;

    @Column(name = "delivery_url", length = 500)
    private String deliveryUrl;

    @Column(name = "delivery_notes", length = 1000)
    private String deliveryNotes;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
