package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

public enum ProjectStatus {
    DRAFT("borrador"),
    PUBLISHED("publicado"),
    IN_PROGRESS("en_curso"),
    COMPLETED("completado"),
    CANCELLED("cancelado");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canPublish() {
        return this == DRAFT;
    }
}