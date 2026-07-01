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
    public Optional<Application> findById(String id) {
        System.out.println("🔍 [REPOSITORY] Buscando ID: '" + id + "'");
        System.out.println("🔍 [REPOSITORY] Longitud del ID: " + (id != null ? id.length() : 0));

        if (id != null) {
            System.out.println("🔍 [REPOSITORY] ID en hex: ");
            for (char c : id.toCharArray()) {
                System.out.print(Integer.toHexString(c) + " ");
            }
            System.out.println();
        }

        var result = persistenceRepository.findById(id);
        System.out.println("🔍 [REPOSITORY] Resultado: " + (result.isPresent() ? "ENCONTRADA ✅" : "NO ENCONTRADA ❌"));

        return result.map(ApplicationPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Application> findByProjectId(String  projectId) {
        return persistenceRepository.findByProjectId(projectId).stream()
                .map(ApplicationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Application> findByUserId(String  userId) {
        return persistenceRepository.findByUserId(userId).stream()
                .map(ApplicationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Application> findByProjectIdAndUserId(String  projectId, String  userId) {
        return persistenceRepository.findByProjectIdAndUserId(projectId, userId).stream()
                .map(ApplicationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsByProjectIdAndUserId(String  projectId, String  userId) {
        return persistenceRepository.existsByProjectIdAndUserId(projectId, userId);
    }
}
