package com.foundly.foundlyplatform.applications.infrastructure.persistence.jpa.adapters;

import com.foundly.foundlyplatform.applications.domain.model.aggregates.Application;
import com.foundly.foundlyplatform.applications.domain.repositories.ApplicationRepository;
import com.foundly.foundlyplatform.applications.infrastructure.persistence.jpa.assemblers.ApplicationPersistenceAssembler;
import com.foundly.foundlyplatform.applications.infrastructure.persistence.jpa.repositories.ApplicationPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter that bridges the Application domain port with Spring Data JPA.
 */
@Repository
public class ApplicationRepositoryImpl implements ApplicationRepository {

    private final ApplicationPersistenceRepository persistenceRepository;

    public ApplicationRepositoryImpl(ApplicationPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Application save(Application application) {
        var entity = ApplicationPersistenceAssembler.toPersistenceFromDomain(application);
        var saved  = persistenceRepository.save(entity);
        return ApplicationPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<Application> findById(Long id) {
        return persistenceRepository.findById(id)
                .map(ApplicationPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Application> findByProjectId(Long projectId) {
        return persistenceRepository.findByProjectId(projectId).stream()
                .map(ApplicationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Application> findByUserId(Long userId) {
        return persistenceRepository.findByUserId(userId).stream()
                .map(ApplicationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Application> findByProjectIdAndUserId(Long projectId, Long userId) {
        return persistenceRepository.findByProjectIdAndUserId(projectId, userId).stream()
                .map(ApplicationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsByProjectIdAndUserId(Long projectId, Long userId) {
        return persistenceRepository.existsByProjectIdAndUserId(projectId, userId);
    }
}
