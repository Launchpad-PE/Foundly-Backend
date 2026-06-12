package com.foundly.foundlyplatform.projects.interfaces.rest;

import com.foundly.foundlyplatform.projects.interfaces.mocks.EnvironmentalMockData;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.DashboardResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/environmental")
@Tag(name = "Environmental Monitoring", description = "IoT and environmental metrics endpoints (MOCK DATA)")
@SecurityRequirement(name = "bearerAuth")
public class EnvironmentalController {

    @GetMapping("/projects/{projectId}/dashboard")
    @Operation(summary = "Get environmental dashboard data for a project (MOCK)")
    public ResponseEntity<DashboardResource> getDashboard(
            @PathVariable String projectId,
            @RequestParam(defaultValue = "7") int days) {

        // Ignoramos projectId y days porque son mock
        return ResponseEntity.ok(EnvironmentalMockData.getMockDashboard());
    }

    @GetMapping("/projects/{projectId}/metrics")
    @Operation(summary = "Get available environmental metrics for a project (MOCK)")
    public ResponseEntity<List<String>> getMetrics(@PathVariable String projectId) {
        // Ignoramos projectId porque es mock
        return ResponseEntity.ok(EnvironmentalMockData.getMockMetricsList());
    }
}
