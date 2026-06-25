package com.foundly.foundlyplatform.messaging.application.internal.commandservices;

import com.foundly.foundlyplatform.messaging.application.commandservices.DirectMessageCommandService;
import com.foundly.foundlyplatform.messaging.domain.model.aggregates.DirectMessage;
import com.foundly.foundlyplatform.messaging.domain.model.commands.SendDirectMessageCommand;
import com.foundly.foundlyplatform.messaging.domain.repositories.DirectMessageRepository;
import com.foundly.foundlyplatform.shared.application.result.ApplicationError;
import com.foundly.foundlyplatform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Application service that handles sending direct messages.
 */
@Service
public class DirectMessageCommandServiceImpl implements DirectMessageCommandService {

    private static final int MAX_CONTENT_LENGTH = 2000;

    private final DirectMessageRepository directMessageRepository;

    public DirectMessageCommandServiceImpl(DirectMessageRepository directMessageRepository) {
        this.directMessageRepository = directMessageRepository;
    }

    @Override
    public Result<DirectMessage, ApplicationError> handle(SendDirectMessageCommand command) {
        if (command.senderId() == null || command.recipientId() == null) {
            return Result.failure(ApplicationError.validationError("message", "Sender and recipient are required"));
        }
        if (command.senderId().equals(command.recipientId())) {
            return Result.failure(ApplicationError.validationError("recipient", "You cannot message yourself"));
        }

        var content = command.content() == null ? "" : command.content().trim();
        if (content.isEmpty()) {
            return Result.failure(ApplicationError.validationError("content", "Message content must not be empty"));
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            return Result.failure(ApplicationError.validationError(
                    "content", "Message content must not exceed %d characters".formatted(MAX_CONTENT_LENGTH)));
        }

        var message = new DirectMessage(command.senderId(), command.recipientId(), content);
        return Result.success(directMessageRepository.save(message));
    }
}
