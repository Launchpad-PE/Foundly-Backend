package com.foundly.foundlyplatform.projects.infrastructure.persistence.jpa.entities;

import com.foundly.foundlyplatform.projects.domain.model.valueobjects.*;
import com.foundly.foundlyplatform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
public class ProjectJpaEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "project_id", nullable = false, unique = true)
    private String projectId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "area", nullable = false)
    private String area;

    @ElementCollection
    @CollectionTable(name = "project_tags", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Column(name = "summary", nullable = false, length = 500)
    private String summary;

    @ElementCollection
    @CollectionTable(name = "project_environmental_metrics", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "metric")
    @Enumerated(EnumType.STRING)
    private List<EnvironmentalMetric> environmentalMetrics = new ArrayList<>();

    @Column(name = "academic_level")
    private String academicLevel;

    @ElementCollection
    @CollectionTable(name = "project_benefits", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "benefit")
    private List<String> benefits = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_skills", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "skill")
    private List<String> requiredSkills = new ArrayList<>();

    @Column(name = "duration_amount")
    private int durationAmount;

    @Column(name = "duration_type")
    @Enumerated(EnumType.STRING)
    private DurationType durationType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(name = "project_author_id", nullable = false)
    private Long authorId;

    @Column(name = "project_author_name")
    private String authorName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private List<ProjectRoleJpaEntity> roles = new ArrayList<>();
}