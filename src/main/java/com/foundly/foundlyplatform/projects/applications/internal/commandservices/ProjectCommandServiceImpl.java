package com.foundly.foundlyplatform.projects.applications.internal.commandservices;

import com.foundly.foundlyplatform.projects.applications.ProjectCommandService;
import com.foundly.foundlyplatform.projects.domain.model.aggregates.Project;
import com.foundly.foundlyplatform.projects.domain.model.commands.*;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.*;
import com.foundly.foundlyplatform.projects.domain.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectCommandServiceImpl implements ProjectCommandService {

    private final ProjectRepository projectRepository;

    public ProjectCommandServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Optional<Project> handle(CreateProjectCommand command) {
        var project = Project.create(
                command.name(),
                command.area(),
                command.tags(),
                command.summary(),
                command.environmentalMetrics(),
                command.academicLevel(),
                command.benefits(),
                command.requiredSkills(),
                command.durationAmount(),
                command.durationType(),
                command.roles(),
                command.authorId(),
                command.authorName()
        );

        return Optional.of(projectRepository.save(project));
    }

    @Override
    public Optional<Project> handle(UpdateProjectCommand command) {
        return projectRepository.findByProjectId(command.projectId())
                .map(project -> {
                    if (command.name() != null) {
                        var name = ProjectName.of(command.name());
                        var area = Area.of(command.area());
                        var summary = Summary.of(command.summary());
                        var tags = project.getTags(); // mantener tags existentes
                        project.updateBasicInfo(name, area, summary, tags);
                    }
                    return projectRepository.save(project);
                });
    }

    @Override
    public Optional<Project> handle(PatchProjectCommand command) {
        return projectRepository.findByProjectId(command.projectId())
                .map(project -> {
                    if (command.name() != null) {
                        project.updateBasicInfo(
                                ProjectName.of(command.name()),
                                Area.of(command.area()),
                                Summary.of(command.summary()),
                                project.getTags()
                        );
                    }
                    if (command.tags() != null) {
                        var tags = command.tags().stream().map(Tag::of).toList();
                        project.updateBasicInfo(project.getName(), project.getArea(), project.getSummary(), tags);
                    }
                    if (command.benefits() != null) {
                        var benefits = command.benefits().stream().map(Benefit::of).toList();
                        project.updateBenefits(benefits);
                    }
                    if (command.requiredSkills() != null || command.durationAmount() != null) {
                        var skills = command.requiredSkills() != null
                                ? command.requiredSkills().stream().map(Skill::of).toList()
                                : project.getRequiredSkills();
                        var duration = command.durationAmount() != null && command.durationType() != null
                                ? Duration.of(command.durationAmount(), command.durationType())
                                : project.getDuration();
                        project.updateSkillsAndDuration(skills, duration);
                    }
                    if (command.environmentalMetrics() != null) {
                        project.updateEnvironmentalMetrics(command.environmentalMetrics());
                    }
                    if (command.status() != null && command.status() == ProjectStatus.PUBLISHED) {
                        project.publish();
                    }
                    return projectRepository.save(project);
                });
    }

    @Override
    public Optional<Project> handle(PublishProjectCommand command) {
        return projectRepository.findByProjectId(command.projectId())
                .map(project -> {
                    project.publish();
                    return projectRepository.save(project);
                });
    }

    @Override
    public Optional<Project> handle(AddRoleCommand command) {
        return projectRepository.findByProjectId(command.projectId())
                .map(project -> {
                    project.addRole(command.role());
                    return projectRepository.save(project);
                });
    }

    @Override
    public Optional<Project> handle(RemoveRoleCommand command) {
        return projectRepository.findByProjectId(command.projectId())
                .map(project -> {
                    project.removeRole(command.roleId());
                    return projectRepository.save(project);
                });
    }

}