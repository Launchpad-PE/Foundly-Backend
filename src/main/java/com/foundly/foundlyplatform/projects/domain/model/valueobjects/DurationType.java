package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

public enum DurationType {
    WEEKS("semanas"),
    MONTHS("meses"),
    SEMESTERS("semestres"),
    YEARS("años");

    private final String displayName;

    DurationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
