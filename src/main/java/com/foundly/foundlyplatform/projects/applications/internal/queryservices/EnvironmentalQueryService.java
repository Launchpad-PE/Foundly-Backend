package com.foundly.foundlyplatform.projects.applications.internal.queryservices;

import com.foundly.foundlyplatform.projects.domain.model.aggregates.Project;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.EnvironmentalMetric;
import com.foundly.foundlyplatform.projects.domain.repositories.ProjectRepository;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.AlertResource;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.DashboardResource;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.MetricCardResource;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.MetricTrendResource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EnvironmentalQueryService {

    private final ProjectRepository projectRepository;
    private final Random random = new Random();

    public EnvironmentalQueryService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public DashboardResource getDashboardData(String projectId, int days) {
        System.out.println("🔍 [EnvironmentalQueryService] Buscando proyecto: " + projectId);

        Optional<Project> projectOpt = projectRepository.findByProjectId(projectId);

        if (projectOpt.isEmpty()) {
            System.out.println("❌ [EnvironmentalQueryService] Proyecto no encontrado: " + projectId);
            return new DashboardResource(List.of(), List.of(), List.of());
        }

        Project project = projectOpt.get();
        List<EnvironmentalMetric> metrics = project.getEnvironmentalMetrics();

        System.out.println("🔍 [EnvironmentalQueryService] Métricas del proyecto: " + metrics);
        System.out.println("🔍 [EnvironmentalQueryService] Cantidad de métricas: " + (metrics != null ? metrics.size() : 0));

        if (metrics == null || metrics.isEmpty()) {
            System.out.println("⚠️ [EnvironmentalQueryService] No hay métricas ambientales para este proyecto");
            return new DashboardResource(List.of(), List.of(), List.of());
        }

        // Generar datos basados en las métricas reales del proyecto
        List<MetricCardResource> metricCards = metrics.stream()
                .map(this::generateMetricCard)
                .collect(Collectors.toList());

        List<MetricTrendResource> trends = metrics.stream()
                .map(metric -> generateTrend(metric, days))
                .collect(Collectors.toList());

        List<AlertResource> alerts = generateAlerts(metrics);

        System.out.println("🔍 [EnvironmentalQueryService] Retornando dashboard con " + metricCards.size() + " métricas");

        return new DashboardResource(metricCards, trends, alerts);
    }

    public List<String> getAvailableMetrics(String projectId) {
        Optional<Project> projectOpt = projectRepository.findByProjectId(projectId);

        if (projectOpt.isEmpty()) {
            return List.of();
        }

        Project project = projectOpt.get();
        return project.getEnvironmentalMetrics().stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    private MetricCardResource generateMetricCard(EnvironmentalMetric metric) {
        double value = generateRealisticValue(metric);
        String unit = getUnit(metric);
        String status = determineStatus(metric, value);
        String icon = getIcon(metric);
        String color = getColor(metric);

        return new MetricCardResource(
                metric.name(),
                String.format("%.0f", value),
                unit,
                status,
                icon,
                color
        );
    }

    private double generateRealisticValue(EnvironmentalMetric metric) {
        switch (metric) {
            case AIR_QUALITY:
                return 50 + random.nextInt(80); // 50-130 AQI
            case HUMIDITY:
                return 40 + random.nextInt(50); // 40-90%
            case TEMPERATURE:
                return 15 + random.nextInt(20); // 15-35°C
            case CITIZEN_PARTICIPATION:
                return 50 + random.nextInt(150); // 50-200 reportes
            default:
                return 50 + random.nextInt(50);
        }
    }

    private String getUnit(EnvironmentalMetric metric) {
        switch (metric) {
            case AIR_QUALITY: return "AQI";
            case HUMIDITY: return "%";
            case TEMPERATURE: return "°C";
            case CITIZEN_PARTICIPATION: return "reportes";
            default: return "";
        }
    }

    private String determineStatus(EnvironmentalMetric metric, double value) {
        switch (metric) {
            case AIR_QUALITY:
                if (value < 50) return "good";
                if (value < 100) return "moderate";
                return "bad";
            case HUMIDITY:
                if (value >= 40 && value <= 70) return "good";
                return "moderate";
            case TEMPERATURE:
                if (value >= 18 && value <= 25) return "normal";
                return "moderate";
            case CITIZEN_PARTICIPATION:
                if (value > 100) return "active";
                return "moderate";
            default:
                return "normal";
        }
    }

    private String getIcon(EnvironmentalMetric metric) {
        switch (metric) {
            case AIR_QUALITY: return "🌬️";
            case HUMIDITY: return "💧";
            case TEMPERATURE: return "🌡️";
            case CITIZEN_PARTICIPATION: return "👥";
            default: return "📊";
        }
    }

    private String getColor(EnvironmentalMetric metric) {
        switch (metric) {
            case AIR_QUALITY: return "#667eea";
            case HUMIDITY: return "#3b82f6";
            case TEMPERATURE: return "#10b981";
            case CITIZEN_PARTICIPATION: return "#8b5cf6";
            default: return "#667eea";
        }
    }

    private MetricTrendResource generateTrend(EnvironmentalMetric metric, int days) {
        List<Integer> values = new ArrayList<>();
        List<String> dayLabels = getDayLabels(days);

        double baseValue = generateRealisticValue(metric);
        for (int i = 0; i < days && i < 7; i++) {
            int variation = random.nextInt(20) - 10;
            int value = (int) Math.max(0, baseValue + variation);
            values.add(value);
        }

        return new MetricTrendResource(metric.name(), values, dayLabels);
    }

    private List<String> getDayLabels(int days) {
        String[] dayNames = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < days && i < dayNames.length; i++) {
            labels.add(dayNames[i]);
        }
        return labels;
    }

    private List<AlertResource> generateAlerts(List<EnvironmentalMetric> metrics) {
        List<AlertResource> alerts = new ArrayList<>();

        for (EnvironmentalMetric metric : metrics) {
            double value = generateRealisticValue(metric);
            String status = determineStatus(metric, value);

            if (status.equals("bad") || status.equals("moderate")) {
                String level = status.equals("bad") ? "red" : "yellow";
                alerts.add(new AlertResource(
                        level,
                        getAlertMessage(metric, value, status),
                        "Hace " + (random.nextInt(5) + 1) + " horas"
                ));
            }
        }

        // Agregar alerta verde si todo está bien
        if (alerts.isEmpty() && !metrics.isEmpty()) {
            alerts.add(new AlertResource(
                    "green",
                    "Todas las métricas dentro de parámetros normales",
                    "Hace 1 hora"
            ));
        }

        return alerts.stream().limit(3).collect(Collectors.toList());
    }

    private String getAlertMessage(EnvironmentalMetric metric, double value, String status) {
        switch (metric) {
            case AIR_QUALITY:
                if (status.equals("bad")) {
                    return "AQI superó nivel " + (int) value + " - Calidad del aire peligrosa";
                }
                return "Nivel de AQI moderado: " + (int) value;
            case HUMIDITY:
                if (value > 75) {
                    return "Humedad excesiva (" + (int) value + "%) - Posible moho";
                }
                if (value < 35) {
                    return "Humedad baja (" + (int) value + "%) - Ambiente seco";
                }
                return "Humedad fuera del rango óptimo (" + (int) value + "%)";
            case TEMPERATURE:
                if (value > 30) {
                    return "Temperatura elevada (" + (int) value + "°C)";
                }
                if (value < 10) {
                    return "Temperatura baja (" + (int) value + "°C)";
                }
                return "Temperatura fuera del rango óptimo (" + (int) value + "°C)";
            case CITIZEN_PARTICIPATION:
                if (value < 50) {
                    return "Baja participación ciudadana (" + (int) value + " reportes)";
                }
                return "Participación ciudadana activa";
            default:
                return "Métrica fuera de parámetros normales";
        }
    }
}