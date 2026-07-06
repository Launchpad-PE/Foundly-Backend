package com.foundly.foundlyplatform.projects.applications.internal.queryservices;
import com.foundly.foundlyplatform.projects.applications.ProjectQueryService;
import com.foundly.foundlyplatform.projects.domain.model.aggregates.Project;
import com.foundly.foundlyplatform.projects.domain.model.queries.*;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.ProjectStatus;
import com.foundly.foundlyplatform.projects.domain.repositories.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectQueryServiceImpl implements ProjectQueryService {

    private final ProjectRepository projectRepository;

    public ProjectQueryServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public Optional<Project> handle(GetProjectByIdQuery query) {
        Optional<Project> project = projectRepository.findByProjectId(query.projectId());

        // ✅ Forzar carga de colecciones
        project.ifPresent(p -> {
            p.getRoles().size();
            p.getRequiredSkills().size();  // ✅ Cambiar getSkills() → getRequiredSkills()
            p.getBenefits().size();
            p.getTags().size();
            p.getEnvironmentalMetrics().size();
        });

        return project;
    }

    @Override
    @Transactional
    public List<Project> handle(GetProjectsByAuthorIdQuery query) {
        List<Project> projects = projectRepository.findByAuthorId(query.authorId());

        // ✅ Forzar carga de colecciones para cada proyecto
        projects.forEach(p -> {
            p.getRoles().size();
            p.getRequiredSkills().size();  // ✅ Cambiar getSkills() → getRequiredSkills()
            p.getBenefits().size();
            p.getTags().size();
            p.getEnvironmentalMetrics().size();
        });

        return projects;
    }

    @Override
    @Transactional
    public List<Project> handle(GetAllPublishedProjectsQuery query) {
        List<Project> projects = projectRepository.findByStatus(ProjectStatus.PUBLISHED);

        // ✅ Forzar carga de colecciones para cada proyecto
        projects.forEach(p -> {
            p.getRoles().size();
            p.getRequiredSkills().size();  // ✅ Cambiar getSkills() → getRequiredSkills()
            p.getBenefits().size();
            p.getTags().size();
            p.getEnvironmentalMetrics().size();
        });

        return projects;
    }

    @Override
    @Transactional
    public List<Project> handle(GetProjectsByAreaQuery query) {
        List<Project> projects = projectRepository.findByArea(query.area());

        // ✅ Forzar carga de colecciones para cada proyecto
        projects.forEach(p -> {
            p.getRoles().size();
            p.getRequiredSkills().size();  // ✅ Cambiar getSkills() → getRequiredSkills()
            p.getBenefits().size();
            p.getTags().size();
            p.getEnvironmentalMetrics().size();
        });

        return projects;
    }
}