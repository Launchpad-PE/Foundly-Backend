package com.foundly.foundlyplatform.projects.interfaces.mocks;

import com.foundly.foundlyplatform.projects.interfaces.rest.resources.AlertResource;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.DashboardResource;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.MetricCardResource;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.MetricTrendResource;

import java.util.List;

public class EnvironmentalMockData {

    private static final List<String> DAYS = List.of("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom");

    public static DashboardResource getMockDashboard() {
        return new DashboardResource(
                getMockMetrics(),
                getMockTrends(),
                getMockAlerts()
        );
    }

    private static List<MetricCardResource> getMockMetrics() {
        return List.of(
                new MetricCardResource("AIR_QUALITY", "72", "AQI", "good", "🌬️", "#667eea"),
                new MetricCardResource("HUMIDITY", "68", "%", "moderate", "💧", "#3b82f6"),
                new MetricCardResource("TEMPERATURE", "23", "°C", "normal", "🌡️", "#10b981"),
                new MetricCardResource("CITIZEN_PARTICIPATION", "142", "reportes", "active", "👥", "#8b5cf6")
        );
    }

    private static List<MetricTrendResource> getMockTrends() {
        return List.of(
                new MetricTrendResource("AIR_QUALITY",
                        List.of(45, 55, 60, 72, 68, 75, 72), DAYS),
                new MetricTrendResource("HUMIDITY",
                        List.of(70, 65, 72, 68, 75, 63, 68), DAYS),
                new MetricTrendResource("TEMPERATURE",
                        List.of(20, 22, 25, 23, 24, 21, 23), DAYS),
                new MetricTrendResource("CITIZEN_PARTICIPATION",
                        List.of(90, 110, 125, 130, 138, 140, 142), DAYS)
        );
    }

    private static List<AlertResource> getMockAlerts() {
        return List.of(
                new AlertResource("red", "AQI superó nivel 100 en zonas norte", "Hoy, 10:32 am"),
                new AlertResource("yellow", "Humedad fuera del rango óptimo (>75%)", "Ayer, 6:15 pm"),
                new AlertResource("green", "Temperatura volvió a rango normal", "Ayer, 2:00 pm")
        );
    }

    public static List<String> getMockMetricsList() {
        return List.of("AIR_QUALITY", "HUMIDITY", "TEMPERATURE", "CITIZEN_PARTICIPATION");
    }
}