package com.foundly.foundlyplatform.projects.interfaces.rest.transform;

import com.foundly.foundlyplatform.projects.domain.model.aggregates.Project;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.CardInfoResource;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.DurationResource;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.ProjectResource;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.RoleResource;

import java.util.stream.Collectors;

public class ProjectResourceFromEntityAssembler {

    public static ProjectResource toResourceFromEntity(Project project) {
        var roles = project.getRoles().stream()
                .map(r -> new RoleResource(
                        r.getName().value(),
                        new CardInfoResource(
                                r.getCardTitle().value(),
                                r.getItemDescriptions()
                        )
                ))
                .collect(Collectors.toList());

        return new ProjectResource(
                project.getProjectIdValue(),
                project.getNameValue(),
                project.getAreaValue(),
                project.getTagValues(),
                project.getSummaryValue(),
                project.getEnvironmentalMetrics().stream().map(Enum::name).collect(Collectors.toList()),
                project.getAcademicLevelValue(),
                project.getBenefitDescriptions(),
                project.getRequiredSkillNames(),
                new DurationResource(project.getDuration().amount(), project.getDuration().type().name()),
                roles,
                project.getStatus().name(),
                project.getAuthorId(),
                project.getAuthorName(),
                project.getCreatedAt() != null ? project.getCreatedAt().toString() : null,
                project.getUpdatedAt() != null ? project.getUpdatedAt().toString() : null
        );
    }
}
