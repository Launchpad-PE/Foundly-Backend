package com.foundly.foundlyplatform.applications.domain.repositories;

import com.foundly.foundlyplatform.applications.domain.model.aggregates.Application;

import java.util.List;
import java.util.Optional;

/**
 * Applications repository port.
 */
public interface ApplicationRepository {

    Application save(Application application);

    Optional<Application> findById(Long id);

    List<Application> findByProjectId(Long projectId);

    List<Application> findByUserId(Long userId);

    List<Application> findByProjectIdAndUserId(Long projectId, Long userId);

    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
}
