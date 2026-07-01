package com.foundly.foundlyplatform.applications.domain.model.aggregates;

import com.foundly.foundlyplatform.applications.domain.model.valueobjects.ApplicationStatus;
import com.foundly.foundlyplatform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static java.util.UUID.randomUUID;

/**
 * Application aggregate root.
 *
 * <p>Represents a collaborator's application to a specific role within a project.
 * An application starts as {@link ApplicationStatus#PENDING} and the project owner
 * can then accept or reject it.</p>
 */
@Getter
public class Application extends AbstractDomainAggregateRoot<Application> {

    @Setter private String id;         // ← Long → String
    @Setter private String projectId;  // ← Long → String
    @Setter private String userId;     // ← Long → String
    @Setter private String roleId;
    @Setter private ApplicationStatus status;
    @Setter private String fullName;
    @Setter private String email;
    @Setter private String portfolioUrl;
    @Setter private String phone;
    @Setter private String cvUrl;
    @Setter private String message;
    @Setter private boolean acceptedTerms;
    @Setter private String avatarUrl;
    @Setter private Date createdAt;
    @Setter private Date updatedAt;

    public Application() {}

    public Application(String projectId, String userId, String roleId,
                       String fullName, String email,
                       String portfolioUrl, String phone,
                       String cvUrl, String message,
                       boolean acceptedTerms, String avatarUrl) {
        this.id = randomUUID().toString();
        this.projectId    = projectId;
        this.userId       = userId;
        this.roleId       = roleId;
        this.fullName     = fullName;
        this.email        = email;
        this.portfolioUrl = portfolioUrl;
        this.phone        = phone;
        this.cvUrl        = cvUrl;
        this.message      = message;
        this.acceptedTerms = acceptedTerms;
        this.avatarUrl    = avatarUrl;
        this.status       = ApplicationStatus.PENDING;
    }

    public void accept() {
        this.status = ApplicationStatus.ACCEPTED;
    }

    public void reject() {
        this.status = ApplicationStatus.REJECTED;
    }

    public boolean isPending()  { return status == ApplicationStatus.PENDING;  }
    public boolean isAccepted() { return status == ApplicationStatus.ACCEPTED; }
    public boolean isRejected() { return status == ApplicationStatus.REJECTED; }
}
