package com.foundly.foundlyplatform.projects.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project_roles")
@Getter
@Setter
@NoArgsConstructor
public class ProjectRoleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Column(name = "card_title", nullable = false)
    private String cardTitle;

    @ElementCollection
    @CollectionTable(name = "project_role_items", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "item")
    private List<String> items = new ArrayList<>();
}