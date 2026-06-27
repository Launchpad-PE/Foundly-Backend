package com.foundly.foundlyplatform.milestones.domain.model.entities;

import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.ChecklistStep;
import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.MilestoneTaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MilestoneTask entity - belongs to a Milestone aggregate.
 * Stored embedded as a JSON column or separate table (see infrastructure layer).
 */
@Getter
@Setter
@NoArgsConstructor
public class MilestoneTask {

    private String id;
    private String milestoneId;
    private String title;
    private String description;
    private String assigneeId;
    private Date dueDate;
    private List<ChecklistStep> checklist = new ArrayList<>();
    private List<String> attachments = new ArrayList<>();
    private MilestoneTaskStatus status = MilestoneTaskStatus.PENDING;
    private String deliveryUrl;
    private String deliveryNotes;
    private Date createdAt;
    private Date updatedAt;

    public MilestoneTask(
            String id,
            String milestoneId,
            String title,
            String description,
            String assigneeId,
            Date dueDate,
            List<ChecklistStep> checklist,
            List<String> attachments
    ) {
        this.id = id;
        this.milestoneId = milestoneId;
        this.title = title;
        this.description = description;
        this.assigneeId = assigneeId;
        this.dueDate = dueDate;
        this.checklist = checklist != null ? new ArrayList<>(checklist) : new ArrayList<>();
        this.attachments = attachments != null ? new ArrayList<>(attachments) : new ArrayList<>();
        this.status = MilestoneTaskStatus.PENDING;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public boolean isCompleted() {
        return MilestoneTaskStatus.COMPLETED.equals(this.status);
    }

    public void complete(String deliveryUrl, String deliveryNotes) {
        this.status = MilestoneTaskStatus.COMPLETED;
        this.deliveryUrl = deliveryUrl;
        this.deliveryNotes = deliveryNotes;
        this.updatedAt = new Date();
    }

    public void reopen() {
        this.status = MilestoneTaskStatus.PENDING;
        this.deliveryUrl = null;
        this.deliveryNotes = null;
        this.updatedAt = new Date();
    }

    public void markAsDelayed() {
        if (!isCompleted()) {
            this.status = MilestoneTaskStatus.DELAYED;
            this.updatedAt = new Date();
        }
    }

    public void updateStatus(MilestoneTaskStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = new Date();
    }
}
