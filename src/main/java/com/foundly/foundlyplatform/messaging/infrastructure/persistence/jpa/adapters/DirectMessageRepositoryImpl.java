package com.foundly.foundlyplatform.messaging.infrastructure.persistence.jpa.adapters;

import com.foundly.foundlyplatform.messaging.domain.model.aggregates.DirectMessage;
import com.foundly.foundlyplatform.messaging.domain.repositories.DirectMessageRepository;
import com.foundly.foundlyplatform.messaging.infrastructure.persistence.jpa.assemblers.DirectMessagePersistenceAssembler;
import com.foundly.foundlyplatform.messaging.infrastructure.persistence.jpa.repositories.DirectMessagePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository adapter that bridges the direct message domain repository port with Spring Data JPA.
 */
@Repository
public class DirectMessageRepositoryImpl implements DirectMessageRepository {

    private final DirectMessagePersistenceRepository directMessagePersistenceRepository;

    public DirectMessageRepositoryImpl(DirectMessagePersistenceRepository directMessagePersistenceRepository) {
        this.directMessagePersistenceRepository = directMessagePersistenceRepository;
    }

    @Override
    public DirectMessage save(DirectMessage message) {
        var saved = directMessagePersistenceRepository.save(
                DirectMessagePersistenceAssembler.toPersistenceFromDomain(message));
        return DirectMessagePersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public List<DirectMessage> findConversation(Long userAId, Long userBId) {
        return directMessagePersistenceRepository.findConversation(userAId, userBId).stream()
                .map(DirectMessagePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<DirectMessage> findAllForUser(Long userId) {
        return directMessagePersistenceRepository.findAllForUser(userId).stream()
                .map(DirectMessagePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
