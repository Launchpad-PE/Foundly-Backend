package com.foundly.foundlyplatform.projects.infrastructure.persistence.jpa.assemblers;

import com.foundly.foundlyplatform.projects.domain.model.entities.ProjectRole;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.CardItem;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.CardTitle;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.RoleName;
import com.foundly.foundlyplatform.projects.infrastructure.persistence.jpa.entities.ProjectRoleJpaEntity;

import java.util.stream.Collectors;

public class ProjectRoleAssembler {

    public static ProjectRole toDomainFromJpa(ProjectRoleJpaEntity entity) {
        if (entity == null) return null;

        var role = new ProjectRole(
                RoleName.of(entity.getRoleName()),
                CardTitle.of(entity.getCardTitle()),
                entity.getItems().stream().map(CardItem::of).collect(Collectors.toList())
        );
        role.setId(entity.getId());
        return role;
    }

    public static ProjectRoleJpaEntity toJpaFromDomain(ProjectRole domain, String projectId) {
        if (domain == null) return null;

        var entity = new ProjectRoleJpaEntity();
        entity.setProjectId(projectId);
        entity.setRoleName(domain.getName().value());
        entity.setCardTitle(domain.getCardTitle().value());
        entity.setItems(domain.getItemDescriptions());

        return entity;
    }
}