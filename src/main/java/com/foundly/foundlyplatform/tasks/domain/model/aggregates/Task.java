package com.foundly.foundlyplatform.tasks.domain.model.aggregates;

import com.foundly.foundlyplatform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.foundly.foundlyplatform.tasks.domain.model.valueobjects.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Task aggregate root.
 *
 * <p>Represents a task assigned to a collaborator within a project.</p>
 */
@Getter
public class Task extends AbstractDomainAggregateRoot<Task> {

    @Setter private Long id;
    @Setter private String projectId;
    @Setter private String assigneeId;
    @Setter private String creatorId;
    @Setter private String title;
    @Setter private String description;
    @Setter private Date dueDate;
    @Setter private List<ChecklistItem> checklist;
    @Setter private List<String> attachments;
    @Setter private List<String> tools;
    @Setter private String comment;
    @Setter private TaskStatus status;
    @Setter private String deliveryUrl;
    @Setter private String deliveryNotes;
    @Setter private Date createdAt;
    @Setter private Date updatedAt;

    public Task() {}

    public Task(String projectId, String assigneeId, String creatorId,
                String title, String description, Date dueDate,
                List<ChecklistItem> checklist, List<String> attachments,
                List<String> tools, String comment) {
        this.projectId = projectId;
        this.assigneeId = assigneeId;
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.checklist = checklist != null ? checklist : List.of();
        this.attachments = attachments != null ? attachments : List.of();
        this.tools = tools != null ? tools : List.of();
        this.comment = comment;
        this.status = TaskStatus.PENDING;
    }

    public record ChecklistItem(String description, boolean done) {}
}
