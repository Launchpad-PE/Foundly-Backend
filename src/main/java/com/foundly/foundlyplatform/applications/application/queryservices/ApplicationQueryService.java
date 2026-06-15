package com.foundly.foundlyplatform.applications.application.queryservices;

import com.foundly.foundlyplatform.applications.domain.model.aggregates.Application;
import com.foundly.foundlyplatform.applications.domain.model.queries.GetApplicationByIdQuery;
import com.foundly.foundlyplatform.applications.domain.model.queries.GetApplicationsByProjectAndUserIdQuery;
import com.foundly.foundlyplatform.applications.domain.model.queries.GetApplicationsByProjectIdQuery;
import com.foundly.foundlyplatform.applications.domain.model.queries.GetApplicationsByUserIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for application queries.
 */
public interface ApplicationQueryService {

    Optional<Application> handle(GetApplicationByIdQuery query);

    List<Application> handle(GetApplicationsByProjectIdQuery query);

    List<Application> handle(GetApplicationsByUserIdQuery query);

    List<Application> handle(GetApplicationsByProjectAndUserIdQuery query);
}
