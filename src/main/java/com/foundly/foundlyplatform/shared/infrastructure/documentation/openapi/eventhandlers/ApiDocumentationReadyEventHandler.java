package com.foundly.foundlyplatform.shared.infrastructure.documentation.openapi.eventhandlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Logs the local access URLs (API base, Swagger UI and OpenAPI docs) once the
 * application is ready, so they can be opened directly from the run console.
 */
@Component
@Slf4j
public class ApiDocumentationReadyEventHandler {

    private final Environment environment;

    public ApiDocumentationReadyEventHandler(Environment environment) {
        this.environment = environment;
    }

    /**
     * Prints a startup banner with the URLs to reach the running application.
     *
     * @param event Spring Boot readiness event
     */
    @EventListener
    public void on(ApplicationReadyEvent event) {
        var applicationName = environment.getProperty("spring.application.name", "Application");
        var port = environment.getProperty("server.port", "8080");
        var contextPath = environment.getProperty("server.servlet.context-path", "");
        var baseUrl = "http://localhost:" + port + contextPath;

        log.info("""

                ----------------------------------------------------------------
                  {} is up and running! Open these in your browser:
                  Local API:    {}
                  Swagger UI:   {}/swagger-ui/index.html
                  OpenAPI docs: {}/v3/api-docs
                ----------------------------------------------------------------""",
                applicationName, baseUrl, baseUrl, baseUrl);
    }
}
