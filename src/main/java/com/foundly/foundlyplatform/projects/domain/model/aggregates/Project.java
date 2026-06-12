package com.foundly.foundlyplatform.projects.domain.model.aggregates;


import com.foundly.foundlyplatform.projects.domain.model.entities.ProjectRole;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.*;
import com.foundly.foundlyplatform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@NoArgsConstructor
public class Project extends AuditableAbstractPersistenceEntity {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "project_id_value", nullable = false, unique = true))
    })
    private ProjectId projectId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "name_value", nullable = false, length = 100))
    })
    private ProjectName name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "area_value", nullable = false))
    })
    private Area area;

    @ElementCollection
    @CollectionTable(name = "project_tags", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "tag_value")
    private List<Tag> tags = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "summary_value", nullable = false, length = 500))
    })
    private Summary summary;

    @ElementCollection
    @CollectionTable(name = "project_environmental_metrics", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "metric")
    @Enumerated(EnumType.STRING)
    private List<EnvironmentalMetric> environmentalMetrics = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "academic_level_value"))
    })
    private AcademicLevel academicLevel;

    @ElementCollection
    @CollectionTable(name = "project_benefits", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "benefit_description")
    private List<Benefit> benefits = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_skills", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "skill_name")
    private List<Skill> requiredSkills = new ArrayList<>();

    @Embedded
    private Duration duration;  // Este ya tiene amount y type, no causa conflicto

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private List<ProjectRole> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private Long authorId;
    private String authorName;

    public Project(
            ProjectId projectId,
            ProjectName name,
            Area area,
            List<Tag> tags,
            Summary summary,
            List<EnvironmentalMetric> environmentalMetrics,
            AcademicLevel academicLevel,
            List<Benefit> benefits,
            List<Skill> requiredSkills,
            Duration duration,
            List<ProjectRole> roles,
            Long authorId,
            String authorName
    ) {
        this.projectId = projectId;
        this.name = name;
        this.area = area;
        this.tags = new ArrayList<>(tags);
        this.summary = summary;
        this.environmentalMetrics = environmentalMetrics != null ? new ArrayList<>(environmentalMetrics) : new ArrayList<>();
        this.academicLevel = academicLevel;
        this.benefits = new ArrayList<>(benefits);
        this.requiredSkills = new ArrayList<>(requiredSkills);
        this.duration = duration;
        this.roles = new ArrayList<>(roles);
        this.status = ProjectStatus.DRAFT;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    // Factory method
    public static Project create(
            String name,
            String area,
            List<String> tags,
            String summary,
            List<EnvironmentalMetric> environmentalMetrics,
            String academicLevel,
            List<String> benefits,
            List<String> requiredSkills,
            int durationAmount,
            DurationType durationType,
            List<ProjectRole> roles,
            Long authorId,
            String authorName
    ) {
        return new Project(
                ProjectId.generate(),
                ProjectName.of(name),
                Area.of(area),
                tags.stream().map(Tag::of).toList(),
                Summary.of(summary),
                environmentalMetrics,
                academicLevel != null ? AcademicLevel.of(academicLevel) : null,
                benefits.stream().map(Benefit::of).toList(),
                requiredSkills.stream().map(Skill::of).toList(),
                Duration.of(durationAmount, durationType),
                roles,
                authorId,
                authorName
        );
    }

    // Business methods
    public void publish() {
        if (status.canPublish()) {
            this.status = ProjectStatus.PUBLISHED;
        } else {
            throw new IllegalStateException("Only draft projects can be published");
        }
    }

    public void updateBasicInfo(ProjectName name, Area area, Summary summary, List<Tag> tags) {
        this.name = name;
        this.area = area;
        this.summary = summary;
        this.tags = new ArrayList<>(tags);
    }

    public void updateSkillsAndDuration(List<Skill> requiredSkills, Duration duration) {
        this.requiredSkills = new ArrayList<>(requiredSkills);
        this.duration = duration;
    }

    public void updateBenefits(List<Benefit> benefits) {
        this.benefits = new ArrayList<>(benefits);
    }

    public void addRole(ProjectRole role) {
        this.roles.add(role);
    }

    public void removeRole(Long roleId) {
        this.roles.removeIf(r -> r.getId().equals(roleId));
    }

    public void updateEnvironmentalMetrics(List<EnvironmentalMetric> metrics) {
        this.environmentalMetrics = new ArrayList<>(metrics);
    }

    // Getters con valores primitivos
    public String getProjectIdValue() {
        return projectId != null ? projectId.value() : null;
    }

    public String getNameValue() {
        return name != null ? name.value() : null;
    }

    public String getAreaValue() {
        return area != null ? area.value() : null;
    }

    public List<String> getTagValues() {
        return tags.stream().map(Tag::value).toList();
    }

    public String getSummaryValue() {
        return summary != null ? summary.value() : null;
    }

    public List<String> getBenefitDescriptions() {
        return benefits.stream().map(Benefit::description).toList();
    }

    public List<String> getRequiredSkillNames() {
        return requiredSkills.stream().map(Skill::name).toList();
    }

    public String getAcademicLevelValue() {
        return academicLevel != null ? academicLevel.value() : null;
    }
}