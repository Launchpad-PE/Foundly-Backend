package com.foundly.foundlyplatform.projects.infrastructure.persistence.jpa.assemblers;

import com.foundly.foundlyplatform.projects.domain.model.aggregates.Project;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.*;
import com.foundly.foundlyplatform.projects.infrastructure.persistence.jpa.entities.ProjectJpaEntity;

import java.util.stream.Collectors;

public class ProjectAssembler {

    public static Project toDomainFromJpa(ProjectJpaEntity entity) {
        if (entity == null) return null;

        var roles = entity.getRoles().stream()
                .map(ProjectRoleAssembler::toDomainFromJpa)
                .collect(Collectors.toList());

        return new Project(
                ProjectId.of(entity.getProjectId()),
                ProjectName.of(entity.getName()),
                Area.of(entity.getArea()),
                entity.getTags().stream().map(Tag::of).collect(Collectors.toList()),
                Summary.of(entity.getSummary()),
                entity.getEnvironmentalMetrics(),
                entity.getAcademicLevel() != null ? AcademicLevel.of(entity.getAcademicLevel()) : null,
                entity.getBenefits().stream().map(Benefit::of).collect(Collectors.toList()),
                entity.getRequiredSkills().stream().map(Skill::of).collect(Collectors.toList()),
                Duration.of(entity.getDurationAmount(), entity.getDurationType()),
                roles,
                entity.getAuthorId(),
                entity.getAuthorName()
        );
    }

    public static ProjectJpaEntity toJpaFromDomain(Project domain) {
        if (domain == null) return null;

        var entity = new ProjectJpaEntity();
        entity.setProjectId(domain.getProjectIdValue());
        entity.setName(domain.getNameValue());
        entity.setArea(domain.getAreaValue());
        entity.setTags(domain.getTagValues());
        entity.setSummary(domain.getSummaryValue());
        entity.setEnvironmentalMetrics(domain.getEnvironmentalMetrics());
        entity.setAcademicLevel(domain.getAcademicLevelValue());
        entity.setBenefits(domain.getBenefitDescriptions());
        entity.setRequiredSkills(domain.getRequiredSkillNames());
        entity.setDurationAmount(domain.getDuration().amount());
        entity.setDurationType(domain.getDuration().type());
        entity.setStatus(domain.getStatus());
        entity.setAuthorId(domain.getAuthorId());
        entity.setAuthorName(domain.getAuthorName());

        return entity;
    }
}