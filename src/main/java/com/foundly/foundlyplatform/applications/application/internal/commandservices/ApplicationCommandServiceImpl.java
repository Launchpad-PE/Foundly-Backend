package com.foundly.foundlyplatform.applications.application.internal.commandservices;

import com.foundly.foundlyplatform.applications.application.commandservices.ApplicationCommandService;
import com.foundly.foundlyplatform.applications.domain.model.aggregates.Application;
import com.foundly.foundlyplatform.applications.domain.model.commands.ApplyToProjectCommand;
import com.foundly.foundlyplatform.applications.domain.model.commands.UpdateApplicationStatusCommand;
import com.foundly.foundlyplatform.applications.domain.repositories.ApplicationRepository;
import com.foundly.foundlyplatform.shared.application.result.ApplicationError;
import com.foundly.foundlyplatform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Application service that handles commands related to project applications.
 */
@Service
public class ApplicationCommandServiceImpl implements ApplicationCommandService {

    private final ApplicationRepository applicationRepository;

    public ApplicationCommandServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Result<Application, ApplicationError> handle(ApplyToProjectCommand command) {
        if (command.projectId() == null || command.userId() == null) {
            return Result.failure(ApplicationError.validationError(
                    "application", "projectId and userId are required"));
        }
        if (command.roleId() == null || command.roleId().isBlank()) {
            return Result.failure(ApplicationError.validationError(
                    "roleId", "A role must be selected"));
        }
        if (command.fullName() == null || command.fullName().isBlank()) {
            return Result.failure(ApplicationError.validationError(
                    "fullName", "Full name must not be empty"));
        }
        if (command.email() == null || command.email().isBlank()) {
            return Result.failure(ApplicationError.validationError(
                    "email", "Email must not be empty"));
        }
        if (command.cvUrl() == null || command.cvUrl().isBlank()) {
            return Result.failure(ApplicationError.validationError(
                    "cvUrl", "CV URL is required"));
        }
        if (command.message() == null || command.message().isBlank()) {
            return Result.failure(ApplicationError.validationError(
                    "message", "Presentation message is required"));
        }
        if (!command.acceptedTerms()) {
            return Result.failure(ApplicationError.validationError(
                    "acceptedTerms", "You must accept the terms to apply"));
        }

        // Ya no necesitamos convertir, ambos son String
        if (applicationRepository.existsByProjectIdAndUserId(command.projectId(), command.userId())) {
            return Result.failure(ApplicationError.conflict(
                    "application", "User has already applied to this project"));
        }

        var application = new Application(
                command.projectId(),
                command.userId(),
                command.roleId(),
                command.fullName().trim(),
                command.email().trim(),
                command.portfolioUrl(),
                command.phone(),
                command.cvUrl().trim(),
                command.message().trim(),
                command.acceptedTerms(),
                command.avatarUrl()
        );
        var saved = applicationRepository.save(application);
        return Result.success(saved);
    }

    @Override
    public Result<Application, ApplicationError> handle(UpdateApplicationStatusCommand command) {
        System.out.println("🔍 [SERVICE] Recibido comando:");
        System.out.println("  - applicationId: '" + command.applicationId() + "'");
        System.out.println("  - status: " + command.status());

        if (command.applicationId() == null) {
            return Result.failure(ApplicationError.validationError(
                    "applicationId", "Application ID is required"));
        }
        if (command.status() == null) {
            return Result.failure(ApplicationError.validationError(
                    "status", "Status is required"));
        }

        var optApplication = applicationRepository.findById(command.applicationId());
        if (optApplication.isEmpty()) {
            return Result.failure(ApplicationError.notFound(
                    "application", String.valueOf(command.applicationId())));
        }

        var application = optApplication.get();
        switch (command.status()) {
            case ACCEPTED -> application.accept();
            case REJECTED -> application.reject();
            default -> {
                return Result.failure(ApplicationError.validationError(
                        "status", "Status must be ACCEPTED or REJECTED"));
            }
        }

        var saved = applicationRepository.save(application);
        return Result.success(saved);
    }
}
