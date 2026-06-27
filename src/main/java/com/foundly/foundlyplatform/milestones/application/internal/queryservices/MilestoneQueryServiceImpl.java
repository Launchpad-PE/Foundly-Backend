package com.foundly.foundlyplatform.milestones.application.internal.queryservices;

import com.foundly.foundlyplatform.milestones.application.queryservices.MilestoneQueryService;
import com.foundly.foundlyplatform.milestones.domain.model.aggregates.Milestone;
import com.foundly.foundlyplatform.milestones.domain.model.entities.MilestoneTask;
import com.foundly.foundlyplatform.milestones.domain.model.queries.*;
import com.foundly.foundlyplatform.milestones.domain.repositories.MilestoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MilestoneQueryServiceImpl implements MilestoneQueryService {

    private final MilestoneRepository milestoneRepository;

    public MilestoneQueryServiceImpl(MilestoneRepository milestoneRepository) {
        this.milestoneRepository = milestoneRepository;
    }

    @Override
    public Optional<Milestone> handle(GetMilestoneByIdQuery query) {
        return milestoneRepository.findById(query.milestoneId());
    }

    @Override
    public List<Milestone> handle(GetMilestonesByProjectIdQuery query) {
        return milestoneRepository.findByProjectId(query.projectId());
    }

    @Override
    public Optional<MilestoneTask> handle(GetTaskByIdQuery query) {
        return milestoneRepository.findTaskById(query.taskId());
    }

    @Override
    public List<MilestoneTask> handle(GetTasksByAssigneeQuery query) {
        // Flatten tasks across all milestones for the assignee
        return milestoneRepository.findByProjectId("") // fetches all — override in impl if needed
                .stream()
                .flatMap(m -> m.getTasks().stream())
                .filter(t -> query.assigneeId().equals(t.getAssigneeId()))
                .toList();
    }
}
