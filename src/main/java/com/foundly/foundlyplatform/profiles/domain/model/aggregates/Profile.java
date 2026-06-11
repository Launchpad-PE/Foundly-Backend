package com.foundly.foundlyplatform.profiles.domain.model.aggregates;

import com.foundly.foundlyplatform.profiles.domain.model.entities.Experience;
import com.foundly.foundlyplatform.profiles.domain.model.valueobjects.Bio;
import com.foundly.foundlyplatform.profiles.domain.model.valueobjects.Role;
import com.foundly.foundlyplatform.profiles.domain.model.valueobjects.Username;
import com.foundly.foundlyplatform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.foundly.foundlyplatform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false)
    private Long userId;

    @Embedded
    @AttributeOverride(name = "username", column = @Column(name = "username", nullable = false, length = 50))
    private Username username;

    @Column
    private String avatar;

    @Embedded
    @AttributeOverride(name = "bio", column = @Column(name = "bio", nullable = false, length = 500))
    private Bio bio;

    @Embedded
    @AttributeOverride(name = "role",column = @Column(name = "role", nullable = false, length = 100))
    private Role role;

    @ElementCollection
    @CollectionTable(name = "profile_skills", joinColumns = @JoinColumn (name = "profile_id"))
    @Column(name = "skills")
    private List<String> skills = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "profile_experiences", joinColumns = @JoinColumn(name = "profile_id"))
    private List<Experience> experiences = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "profile_favorite_projects", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "project_id")
    private List<String> favoriteProjectIds = new ArrayList<>();

    @Column(nullable = false)
    private boolean isComplete = false;

    public Profile(Long userId, Username username, Bio bio, Role role) {
        this.userId = userId;
        this.username = username;
        this.bio = bio;
        this.role = role;

    }

    public void updateUsername(String newUsername){
        this.username = Username.of(newUsername);
    }

    public void updateBio(String newBio){
        this.bio = Bio.of(newBio);
    }

    public void updateRole(String newRole){
        this.role = Role.of(newRole);
    }

    public void updateAvatar(String avatarUrl){
        this.avatar = avatarUrl;
    }

    public void addSkill(String skill){
        if(!this.skills.contains(skill))
            this.skills.remove(skill);
    }

    public void removeSkill(String skill) {
        this.skills.remove(skill);
    }

    public void addExperience(Experience experience) {
        this.experiences.add(experience);
    }

    public void removeExperience(String experienceId) {
        this.experiences.removeIf(e -> e.getExperienceId().equals(experienceId));
    }

    public void addFavoriteProject(String projectId) {
        if (!this.favoriteProjectIds.contains(projectId))
            this.favoriteProjectIds.add(projectId);
    }

    public void removeFavoriteProject(String projectId) {
        this.favoriteProjectIds.remove(projectId);
    }

    public void completeProfile() {
        this.isComplete = true;
    }

    public String getUsernameValue() {
        return username != null ? username.username() : null;
    }

    public String getBioValue() {
        return bio != null ? bio.bio() : null;
    }

    public String getRoleValue() {
        return role != null ? role.role() : null;
    }
}