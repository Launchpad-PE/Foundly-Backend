package com.foundly.foundlyplatform.messaging.application.internal.queryservices;

import com.foundly.foundlyplatform.messaging.application.queryservices.DirectMessageQueryService;
import com.foundly.foundlyplatform.messaging.domain.model.aggregates.DirectMessage;
import com.foundly.foundlyplatform.messaging.domain.model.queries.GetConversationQuery;
import com.foundly.foundlyplatform.messaging.domain.model.queries.GetConversationsForUserQuery;
import com.foundly.foundlyplatform.messaging.domain.repositories.DirectMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service that resolves direct message read queries.
 */
@Service
public class DirectMessageQueryServiceImpl implements DirectMessageQueryService {

    private final DirectMessageRepository directMessageRepository;

    public DirectMessageQueryServiceImpl(DirectMessageRepository directMessageRepository) {
        this.directMessageRepository = directMessageRepository;
    }

    @Override
    public List<DirectMessage> handle(GetConversationQuery query) {
        return directMessageRepository.findConversation(query.userAId(), query.userBId());
    }

    @Override
    public List<DirectMessage> handle(GetConversationsForUserQuery query) {
        return directMessageRepository.findAllForUser(query.userId());
    }
}
