package com.foundly.foundlyplatform.applications.domain.repositories;

import com.foundly.foundlyplatform.applications.domain.model.aggregates.Application;

import java.util.List;
import java.util.Optional;

/**
 * Applications repository port.
 */
public interface ApplicationRepository {

    Application save(Application application);

    Optional<Application> findById(String id);  // ← Long → String

    List<Application> findByProjectId(String projectId);  // ← Long → String

    List<Application> findByUserId(String userId);  // ← Long → String

    List<Application> findByProjectIdAndUserId(String projectId, String userId);

    boolean existsByProjectIdAndUserId(String projectId, String userId);  // ← Long → String
}
