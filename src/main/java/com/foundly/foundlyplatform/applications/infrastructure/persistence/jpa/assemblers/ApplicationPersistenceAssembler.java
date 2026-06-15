package com.foundly.foundlyplatform.applications.infrastructure.persistence.jpa.assemblers;

import com.foundly.foundlyplatform.applications.domain.model.aggregates.Application;
import com.foundly.foundlyplatform.applications.infrastructure.persistence.jpa.entities.ApplicationPersistenceEntity;

/**
 * Static assembler between the Application domain aggregate and its JPA persistence entity.
 */
public final class ApplicationPersistenceAssembler {

    private ApplicationPersistenceAssembler() {}

    public static Application toDomainFromPersistence(ApplicationPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new Application();
        domain.setId(entity.getId());
        domain.setProjectId(entity.getProjectId());
        domain.setUserId(entity.getUserId());
        domain.setRoleId(entity.getRoleId());
        domain.setStatus(entity.getStatus());
        domain.setFullName(entity.getFullName());
        domain.setEmail(entity.getEmail());
        domain.setPortfolioUrl(entity.getPortfolioUrl());
        domain.setPhone(entity.getPhone());
        domain.setCvUrl(entity.getCvUrl());
        domain.setMessage(entity.getMessage());
        domain.setAcceptedTerms(entity.isAcceptedTerms());
        domain.setAvatarUrl(entity.getAvatarUrl());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        return domain;
    }

    public static ApplicationPersistenceEntity toPersistenceFromDomain(Application domain) {
        if (domain == null) return null;
        var entity = new ApplicationPersistenceEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setProjectId(domain.getProjectId());
        entity.setUserId(domain.getUserId());
        entity.setRoleId(domain.getRoleId());
        entity.setStatus(domain.getStatus());
        entity.setFullName(domain.getFullName());
        entity.setEmail(domain.getEmail());
        entity.setPortfolioUrl(domain.getPortfolioUrl());
        entity.setPhone(domain.getPhone());
        entity.setCvUrl(domain.getCvUrl());
        entity.setMessage(domain.getMessage());
        entity.setAcceptedTerms(domain.isAcceptedTerms());
        entity.setAvatarUrl(domain.getAvatarUrl());
        return entity;
    }
}
