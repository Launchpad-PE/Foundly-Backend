package com.foundly.foundlyplatform.projects.interfaces.rest.transform;


import com.foundly.foundlyplatform.projects.domain.model.commands.CreateProjectCommand;
import com.foundly.foundlyplatform.projects.domain.model.entities.ProjectRole;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.CardItem;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.CardTitle;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.DurationType;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.EnvironmentalMetric;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.RoleName;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.CreateProjectResource;

import java.util.List;
import java.util.stream.Collectors;

public class CreateProjectCommandFromResourceAssembler {

    public static CreateProjectCommand toCommandFromResource(CreateProjectResource resource, Long authorId, String authorName) {
        List<EnvironmentalMetric> metrics = null;
        if (resource.environmentalMetrics() != null) {
            metrics = resource.environmentalMetrics().stream()
                    .map(EnvironmentalMetric::valueOf)
                    .collect(Collectors.toList());
        }

        DurationType durationType = DurationType.valueOf(resource.durationType().toUpperCase());

        List<ProjectRole> roles = null;
        if (resource.roles() != null) {
            roles = resource.roles().stream()
                    .map(r -> new ProjectRole(
                            RoleName.of(r.name()),
                            CardTitle.of(r.cardInfo().title()),
                            r.cardInfo().items().stream().map(CardItem::of).collect(Collectors.toList())
                    ))
                    .collect(Collectors.toList());
        }

        return new CreateProjectCommand(
                resource.name(),
                resource.area(),
                resource.tags(),
                resource.summary(),
                metrics,
                resource.academicLevel(),
                resource.benefits(),
                resource.requiredSkills(),
                resource.durationAmount(),
                durationType,
                roles,
                authorId,
                authorName
        );
    }
}