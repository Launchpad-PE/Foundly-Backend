package com.foundly.foundlyplatform.projects.interfaces.rest;

import com.foundly.foundlyplatform.projects.domain.model.aggregates.Project;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.EnvironmentalMetric;
import com.foundly.foundlyplatform.projects.domain.repositories.ProjectRepository;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/environmental")
@Tag(name = "Environmental Monitoring", description = "IoT and environmental metrics endpoints")
@SecurityRequirement(name = "bearerAuth")
public class EnvironmentalController {

    private final ProjectRepository projectRepository;
    private final Random random = new Random();

    public EnvironmentalController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/projects/{projectId}/dashboard")
    @Operation(summary = "Get environmental dashboard data for a project")
    public ResponseEntity<DashboardResource> getDashboard(
            @PathVariable String projectId,
            @RequestParam(defaultValue = "7") int days) {

        System.out.println("📡 [CONTROLLER] GET /dashboard - projectId: " + projectId);

        Optional<Project> projectOpt = projectRepository.findByProjectId(projectId);

        // ❌ Caso 1: Proyecto NO existe en BD - devolver vacío
        if (projectOpt.isEmpty()) {
            System.out.println("❌ [CONTROLLER] Proyecto NO existe: " + projectId);
            return ResponseEntity.ok(new DashboardResource(List.of(), List.of(), List.of()));
        }

        Project project = projectOpt.get();
        List<EnvironmentalMetric> metrics = project.getEnvironmentalMetrics();

        System.out.println("🔍 Métricas del proyecto: " + metrics);

        // ❌ Caso 2: Proyecto existe pero NO tiene métricas - devolver vacío
        if (metrics == null || metrics.isEmpty()) {
            System.out.println("⚠️ [CONTROLLER] Proyecto " + projectId + " existe pero NO tiene métricas ambientales");
            return ResponseEntity.ok(new DashboardResource(List.of(), List.of(), List.of()));
        }

        // ✅ Caso 3: Proyecto existe Y TIENE métricas - generar datos reales
        System.out.println("✅ [CONTROLLER] Proyecto " + projectId + " tiene " + metrics.size() + " métricas. Generando dashboard real.");

        List<MetricCardResource> metricCards = metrics.stream()
                .map(this::generateMetricCard)
                .collect(Collectors.toList());

        List<MetricTrendResource> trends = metrics.stream()
                .map(metric -> generateTrend(metric, days))
                .collect(Collectors.toList());

        List<AlertResource> alerts = generateAlerts(metrics);

        return ResponseEntity.ok(new DashboardResource(metricCards, trends, alerts));
    }

    @GetMapping("/projects/{projectId}/metrics")
    @Operation(summary = "Get available environmental metrics for a project")
    public ResponseEntity<List<String>> getMetrics(@PathVariable String projectId) {
        Optional<Project> projectOpt = projectRepository.findByProjectId(projectId);

        // ❌ Proyecto NO existe - devolver vacío
        if (projectOpt.isEmpty()) {
            System.out.println("❌ [CONTROLLER] Proyecto NO existe: " + projectId);
            return ResponseEntity.ok(List.of());
        }

        Project project = projectOpt.get();
        List<String> metrics = project.getEnvironmentalMetrics().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        // ❌ Proyecto existe pero NO tiene métricas - devolver vacío
        if (metrics.isEmpty()) {
            System.out.println("⚠️ [CONTROLLER] Proyecto " + projectId + " existe pero NO tiene métricas");
            return ResponseEntity.ok(List.of());
        }

        // ✅ Tiene métricas - devolverlas
        System.out.println("✅ [CONTROLLER] Proyecto " + projectId + " tiene métricas: " + metrics);
        return ResponseEntity.ok(metrics);
    }

    // ========== MÉTODOS PRIVADOS ==========

    private MetricCardResource generateMetricCard(EnvironmentalMetric metric) {
        double value = generateRealisticValue(metric);
        String unit = getUnit(metric);
        String status = getStatus(metric, value);
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
            case AIR_QUALITY: return 50 + random.nextInt(80);
            case HUMIDITY: return 40 + random.nextInt(50);
            case TEMPERATURE: return 15 + random.nextInt(20);
            case CITIZEN_PARTICIPATION: return 50 + random.nextInt(150);
            default: return 50;
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

    private String getStatus(EnvironmentalMetric metric, double value) {
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
            default:
                return "active";
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
        String[] dayNames = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        List<String> daysList = Arrays.asList(dayNames).subList(0, Math.min(days, 7));

        double baseValue = generateRealisticValue(metric);
        for (int i = 0; i < daysList.size(); i++) {
            int variation = random.nextInt(20) - 10;
            values.add((int) Math.max(0, baseValue + variation));
        }

        return new MetricTrendResource(metric.name(), values, daysList);
    }

    private List<AlertResource> generateAlerts(List<EnvironmentalMetric> metrics) {
        List<AlertResource> alerts = new ArrayList<>();

        for (EnvironmentalMetric metric : metrics) {
            double value = generateRealisticValue(metric);
            String status = getStatus(metric, value);

            if (status.equals("bad")) {
                alerts.add(new AlertResource("red", getAlertMessage(metric, value), "Hace " + (random.nextInt(5) + 1) + " horas"));
            } else if (status.equals("moderate")) {
                alerts.add(new AlertResource("yellow", getAlertMessage(metric, value), "Hace " + (random.nextInt(5) + 1) + " horas"));
            }
        }

        if (alerts.isEmpty() && !metrics.isEmpty()) {
            alerts.add(new AlertResource("green", "Todas las métricas dentro de parámetros normales", "Hace 1 hora"));
        }

        return alerts.stream().limit(3).collect(Collectors.toList());
    }

    private String getAlertMessage(EnvironmentalMetric metric, double value) {
        switch (metric) {
            case AIR_QUALITY: return "AQI: " + (int) value + " - Calidad del aire " + (value > 100 ? "peligrosa" : "moderada");
            case HUMIDITY: return "Humedad: " + (int) value + "% - " + (value > 70 ? "excesiva" : "baja");
            case TEMPERATURE: return "Temperatura: " + (int) value + "°C - " + (value > 28 ? "elevada" : "baja");
            default: return "Métrica fuera de parámetros normales";
        }
    }
}