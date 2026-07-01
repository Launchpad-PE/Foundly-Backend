package com.foundly.foundlyplatform.applications.infrastructure.persistence.jpa.entities;

import com.foundly.foundlyplatform.applications.domain.model.valueobjects.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * JPA persistence entity for project applications.
 *
 * NOTA: No extiende de AuditableAbstractPersistenceEntity porque usamos String como ID (UUID)
 * en lugar de Long.
 */
@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
public class ApplicationPersistenceEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "project_id", nullable = false, length = 36)
    private String projectId;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "role_id", nullable = false, length = 100)
    private String roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "portfolio_url", length = 500)
    private String portfolioUrl;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "cv_url", nullable = false, length = 500)
    private String cvUrl;

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @Column(name = "accepted_terms", nullable = false)
    private boolean acceptedTerms;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    // ─── Campos de auditoría (copiados de AuditableAbstractPersistenceEntity) ───

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // ─── Lifecycle callbacks para auditoría ───────────────────────────

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}