package com.foundly.foundlyplatform.milestones.domain.model.aggregates;

import com.foundly.foundlyplatform.milestones.domain.model.entities.MilestoneTask;
import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.MilestoneStatus;
import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.MilestoneTaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Milestone aggregate root.
 *
 * <p>A Milestone represents a deliverable checkpoint within a project.
 * It groups a set of tasks that collaborators must complete by a due date.
 * Status is derived automatically from task completion and due date.</p>
 *
 * <p>This is a pure domain object — no JPA annotations here.
 * Persistence is handled by the infrastructure layer.</p>
 */
@Getter
@Setter
@NoArgsConstructor
public class Milestone {

    private String id;
    private String projectId;
    private String creatorId;
    private String title;
    private String description;
    private List<String> tools = new ArrayList<>();
    private String generalComment;
    private Date dueDate;
    private List<String> attachments = new ArrayList<>();
    private List<MilestoneTask> tasks = new ArrayList<>();
    private MilestoneStatus status = MilestoneStatus.PENDING;
    private String deliveryUrl;
    private String deliveryNotes;
    private Date createdAt;
    private Date updatedAt;

    public Milestone(
            String projectId,
            String creatorId,
            String title,
            String description,
            Date dueDate,
            List<String> tools,
            String generalComment,
            List<String> attachments
    ) {
        this.id = UUID.randomUUID().toString();
        this.projectId = projectId;
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.tools = tools != null ? new ArrayList<>(tools) : new ArrayList<>();
        this.generalComment = generalComment;
        this.attachments = attachments != null ? new ArrayList<>(attachments) : new ArrayList<>();
        this.status = MilestoneStatus.PENDING;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // ─── Task management ───────────────────────────────────────────────────────

    public MilestoneTask addTask(
            String taskTitle,
            String taskDescription,
            String assigneeId
    ) {
        var task = new MilestoneTask(
                UUID.randomUUID().toString(),
                this.id,
                taskTitle,
                taskDescription,
                assigneeId,
                this.dueDate,
                new ArrayList<>(),
                new ArrayList<>()
        );
        this.tasks.add(task);
        this.updatedAt = new Date();
        recalculateStatus();
        return task;
    }

    public void removeTask(String taskId) {
        this.tasks.removeIf(t -> t.getId().equals(taskId));
        this.updatedAt = new Date();
        recalculateStatus();
    }

    public void completeTask(String taskId, String deliveryUrl, String deliveryNotes) {
        findTaskById(taskId).complete(deliveryUrl, deliveryNotes);
        this.updatedAt = new Date();
        recalculateStatus();
    }

    public void reopenTask(String taskId) {
        findTaskById(taskId).reopen();
        this.updatedAt = new Date();
        recalculateStatus();
    }

    public void updateTaskStatus(String taskId, MilestoneTaskStatus newStatus) {
        findTaskById(taskId).updateStatus(newStatus);
        this.updatedAt = new Date();
        recalculateStatus();
    }

    // ─── Milestone business methods ────────────────────────────────────────────

    public void reschedule(Date newDueDate) {
        if (MilestoneStatus.COMPLETED.equals(this.status)) {
            throw new IllegalStateException("Cannot reschedule a completed milestone");
        }
        this.dueDate = newDueDate;
        // Propagate due date to all non-completed tasks
        for (MilestoneTask task : this.tasks) {
            if (!task.isCompleted()) {
                task.setDueDate(newDueDate);
            }
        }
        this.updatedAt = new Date();
        recalculateStatus();
    }

    public void updateInfo(
            String title,
            String description,
            List<String> tools,
            String generalComment,
            List<String> attachments
    ) {
        if (title != null && !title.isBlank()) this.title = title;
        if (description != null && !description.isBlank()) this.description = description;
        if (tools != null) this.tools = new ArrayList<>(tools);
        this.generalComment = generalComment;
        if (attachments != null) this.attachments = new ArrayList<>(attachments);
        this.updatedAt = new Date();
    }

    // ─── Status recalculation ──────────────────────────────────────────────────

    /**
     * Derives milestone status from its tasks and due date.
     * Rules:
     *  1. If all tasks are completed → COMPLETED
     *  2. Else if due date has passed → DELAYED
     *  3. Else → PENDING
     */
    public void recalculateStatus() {
        if (MilestoneStatus.COMPLETED.equals(this.status)) return;

        boolean allCompleted = !this.tasks.isEmpty()
                && this.tasks.stream().allMatch(MilestoneTask::isCompleted);

        if (allCompleted) {
            this.status = MilestoneStatus.COMPLETED;
            return;
        }

        Date now = new Date();
        if (this.dueDate != null && now.after(this.dueDate)) {
            this.status = MilestoneStatus.DELAYED;
        } else {
            this.status = MilestoneStatus.PENDING;
        }
    }

    public int getTasksCompletionPercentage() {
        if (this.tasks.isEmpty()) return 0;
        long completed = this.tasks.stream().filter(MilestoneTask::isCompleted).count();
        return (int) ((completed * 100) / this.tasks.size());
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private MilestoneTask findTaskById(String taskId) {
        return this.tasks.stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
    }
}
