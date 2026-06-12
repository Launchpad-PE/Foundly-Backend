package com.foundly.foundlyplatform.projects.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CreateProjectResource {

    @NotBlank @Size(min = 3, max = 100)
    private String name;

    @NotBlank
    private String area;

    private List<String> tags;

    @NotBlank @Size(min = 10, max = 500)
    private String summary;

    // ✅ Cambiado a environmentalImpact
    private List<String> environmentalImpact;

    private String academicLevel;

    private List<String> benefits;

    private List<String> requiredSkills;

    @NotNull
    private Integer durationAmount;

    @NotNull
    private String durationType;

    private List<RoleResource> roles;

    public CreateProjectResource() {}

    public CreateProjectResource(String name, String area, List<String> tags, String summary,
                                 List<String> environmentalImpact, String academicLevel,
                                 List<String> benefits, List<String> requiredSkills,
                                 Integer durationAmount, String durationType, List<RoleResource> roles) {
        this.name = name;
        this.area = area;
        this.tags = tags;
        this.summary = summary;
        this.environmentalImpact = environmentalImpact;
        this.academicLevel = academicLevel;
        this.benefits = benefits;
        this.requiredSkills = requiredSkills;
        this.durationAmount = durationAmount;
        this.durationType = durationType;
        this.roles = roles;
    }

    // Getters
    public String getName() { return name; }
    public String getArea() { return area; }
    public List<String> getTags() { return tags; }
    public String getSummary() { return summary; }
    public List<String> getEnvironmentalImpact() {return environmentalImpact;}    public String getAcademicLevel() { return academicLevel; }
    public List<String> getBenefits() { return benefits; }
    public List<String> getRequiredSkills() { return requiredSkills; }
    public Integer getDurationAmount() { return durationAmount; }
    public String getDurationType() { return durationType; }
    public List<RoleResource> getRoles() { return roles; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setArea(String area) { this.area = area; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setEnvironmentalImpact(List<String> environmentalImpact) { this.environmentalImpact = environmentalImpact; }
    public void setAcademicLevel(String academicLevel) { this.academicLevel = academicLevel; }
    public void setBenefits(List<String> benefits) { this.benefits = benefits; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
    public void setDurationAmount(Integer durationAmount) { this.durationAmount = durationAmount; }
    public void setDurationType(String durationType) { this.durationType = durationType; }
    public void setRoles(List<RoleResource> roles) { this.roles = roles; }
}