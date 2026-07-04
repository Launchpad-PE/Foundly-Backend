package com.foundly.foundlyplatform.milestones.infrastructure.persistence.jpa.entities;

import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.MilestoneStatus;
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
@Table(name = "milestones")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MilestonePersistenceEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "project_id", nullable = false, length = 36)
    private String projectId;

    @Column(name = "creator_id", nullable = false, length = 100)
    private String creatorId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "milestone_tools", joinColumns = @JoinColumn(name = "milestone_id"))
    @Column(name = "tool_name", length = 100)
    private List<String> tools = new ArrayList<>();

    @Column(name = "general_comment", length = 1000)
    private String generalComment;

    @Column(name = "due_date", nullable = false)
    private Date dueDate;

    @ElementCollection
    @CollectionTable(name = "milestone_attachments", joinColumns = @JoinColumn(name = "milestone_id"))
    @Column(name = "attachment_url", length = 500)
    private List<String> attachments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MilestoneStatus status = MilestoneStatus.PENDING;

    @Column(name = "delivery_url", length = 500)
    private String deliveryUrl;

    @Column(name = "delivery_notes", length = 1000)
    private String deliveryNotes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "milestone_id", nullable = false)
    private List<MilestoneTaskPersistenceEntity> tasks = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
