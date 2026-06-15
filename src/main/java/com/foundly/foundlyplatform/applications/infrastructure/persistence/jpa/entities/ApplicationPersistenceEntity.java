package com.foundly.foundlyplatform.applications.infrastructure.persistence.jpa.entities;

import com.foundly.foundlyplatform.applications.domain.model.valueobjects.ApplicationStatus;
import com.foundly.foundlyplatform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA persistence entity for project applications.
 */
@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
public class ApplicationPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

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
}
