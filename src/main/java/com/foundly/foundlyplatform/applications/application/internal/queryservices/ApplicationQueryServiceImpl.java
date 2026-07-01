package com.foundly.foundlyplatform.applications.application.internal.queryservices;

import com.foundly.foundlyplatform.applications.application.queryservices.ApplicationQueryService;
import com.foundly.foundlyplatform.applications.domain.model.aggregates.Application;
import com.foundly.foundlyplatform.applications.domain.model.queries.GetApplicationByIdQuery;
import com.foundly.foundlyplatform.applications.domain.model.queries.GetApplicationsByProjectAndUserIdQuery;
import com.foundly.foundlyplatform.applications.domain.model.queries.GetApplicationsByProjectIdQuery;
import com.foundly.foundlyplatform.applications.domain.model.queries.GetApplicationsByUserIdQuery;
import com.foundly.foundlyplatform.applications.domain.repositories.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service that handles application query operations.
 */
@Service
public class ApplicationQueryServiceImpl implements ApplicationQueryService {

    private final ApplicationRepository applicationRepository;

    public ApplicationQueryServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Optional<Application> handle(GetApplicationByIdQuery query) {
        return applicationRepository.findById(query.applicationId());
    }

    @Override
    public List<Application> handle(GetApplicationsByProjectIdQuery query) {
        return applicationRepository.findByProjectId(query.projectId());
    }

    @Override
    public List<Application> handle(GetApplicationsByUserIdQuery query) {
        return applicationRepository.findByUserId(query.userId());
    }

    @Override
    public List<Application> handle(GetApplicationsByProjectAndUserIdQuery query) {
        return applicationRepository.findByProjectIdAndUserId(query.projectId(), query.userId());  // ← String
    }
}
