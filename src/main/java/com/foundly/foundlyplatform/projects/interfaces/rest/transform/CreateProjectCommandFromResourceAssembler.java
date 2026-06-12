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
        System.out.println("🔍 [ASSEMBLER] environmental_metrics (strings): " + resource.getEnvironmentalImpact());

        List<EnvironmentalMetric> metrics = null;
        if (resource.getEnvironmentalImpact() != null && !resource.getEnvironmentalImpact().isEmpty()) {
            metrics = resource.getEnvironmentalImpact().stream()
                    .map(metricName -> {
                        try {
                            // ✅ Asegúrate que los strings coincidan con los enum
                            // Ej: "AIR_QUALITY" -> EnvironmentalMetric.AIR_QUALITY
                            return EnvironmentalMetric.valueOf(metricName);
                        } catch (IllegalArgumentException e) {
                            System.err.println("❌ Error convirtiendo métrica: " + metricName);
                            return null;
                        }
                    })
                    .filter(metric -> metric != null)
                    .collect(Collectors.toList());

            System.out.println("🔍 [ASSEMBLER] metrics convertidos: " + metrics);
        }

        DurationType durationType = DurationType.valueOf(resource.getDurationType().toUpperCase());

        List<ProjectRole> roles = null;
        if (resource.getRoles() != null) {
            roles = resource.getRoles().stream()
                    .map(r -> new ProjectRole(
                            RoleName.of(r.name()),
                            CardTitle.of(r.cardInfo().title()),
                            r.cardInfo().items().stream().map(CardItem::of).collect(Collectors.toList())
                    ))
                    .collect(Collectors.toList());
        }

        return new CreateProjectCommand(
                resource.getName(),
                resource.getArea(),
                resource.getTags(),
                resource.getSummary(),
                metrics,  // ← Usar metrics convertido
                resource.getAcademicLevel(),
                resource.getBenefits(),
                resource.getRequiredSkills(),
                resource.getDurationAmount(),
                durationType,
                roles,
                authorId,
                authorName
        );
    }
}