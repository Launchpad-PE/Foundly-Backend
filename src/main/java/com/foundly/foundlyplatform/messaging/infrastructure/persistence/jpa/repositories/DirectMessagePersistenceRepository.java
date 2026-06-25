package com.foundly.foundlyplatform.messaging.infrastructure.persistence.jpa.repositories;

import com.foundly.foundlyplatform.messaging.infrastructure.persistence.jpa.entities.DirectMessagePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for direct message persistence entities.
 */
@Repository
public interface DirectMessagePersistenceRepository extends JpaRepository<DirectMessagePersistenceEntity, Long> {

    /** Messages exchanged between two users, oldest first. */
    @Query("""
            SELECT m FROM DirectMessagePersistenceEntity m
            WHERE (m.senderId = :a AND m.recipientId = :b)
               OR (m.senderId = :b AND m.recipientId = :a)
            ORDER BY m.createdAt ASC
            """)
    List<DirectMessagePersistenceEntity> findConversation(@Param("a") Long userAId, @Param("b") Long userBId);

    /** Every message where the user is sender or recipient, newest first. */
    @Query("""
            SELECT m FROM DirectMessagePersistenceEntity m
            WHERE m.senderId = :userId OR m.recipientId = :userId
            ORDER BY m.createdAt DESC
            """)
    List<DirectMessagePersistenceEntity> findAllForUser(@Param("userId") Long userId);
}
