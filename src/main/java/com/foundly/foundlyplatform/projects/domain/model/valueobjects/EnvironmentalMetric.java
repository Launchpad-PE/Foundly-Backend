package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

public enum EnvironmentalMetric {
    AIR_QUALITY("Calidad del aire"),
    HUMIDITY("Humedad ambiental"),
    TEMPERATURE("Temperatura"),
    CITIZEN_PARTICIPATION("Participación ciudadana");

    private final String displayName;

    EnvironmentalMetric(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}