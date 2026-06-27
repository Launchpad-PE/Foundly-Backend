package com.foundly.foundlyplatform.milestones.infrastructure.persistence.jpa.adapters;

import com.foundly.foundlyplatform.milestones.domain.model.aggregates.Milestone;
import com.foundly.foundlyplatform.milestones.domain.model.entities.MilestoneTask;
import com.foundly.foundlyplatform.milestones.domain.repositories.MilestoneRepository;
import com.foundly.foundlyplatform.milestones.infrastructure.persistence.jpa.assemblers.MilestonePersistenceAssembler;
import com.foundly.foundlyplatform.milestones.infrastructure.persistence.jpa.repositories.MilestonePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MilestoneRepositoryImpl implements MilestoneRepository {

    private final MilestonePersistenceRepository jpaRepository;

    public MilestoneRepositoryImpl(MilestonePersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Milestone save(Milestone milestone) {
        var entity = MilestonePersistenceAssembler.toPersistenceFromDomain(milestone);
        var saved = jpaRepository.save(entity);
        return MilestonePersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<Milestone> findById(String milestoneId) {
        return jpaRepository.findById(milestoneId)
                .map(MilestonePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Milestone> findByProjectId(String projectId) {
        return jpaRepository.findByProjectId(projectId).stream()
                .map(MilestonePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public void deleteById(String milestoneId) {
        jpaRepository.deleteById(milestoneId);
    }

    @Override
    public Optional<MilestoneTask> findTaskById(String taskId) {
        return jpaRepository.findAll().stream()
                .flatMap(m -> m.getTasks().stream())
                .filter(t -> t.getId().equals(taskId))
                .map(MilestonePersistenceAssembler::taskToDomainFromPersistence)
                .findFirst();
    }

    @Override
    public Optional<Milestone> findMilestoneByTaskId(String taskId) {
        return jpaRepository.findAll().stream()
                .filter(m -> m.getTasks().stream().anyMatch(t -> t.getId().equals(taskId)))
                .map(MilestonePersistenceAssembler::toDomainFromPersistence)
                .findFirst();
    }
}
