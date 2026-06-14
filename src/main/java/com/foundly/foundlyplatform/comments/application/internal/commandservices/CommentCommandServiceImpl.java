package com.foundly.foundlyplatform.comments.application.internal.commandservices;

import com.foundly.foundlyplatform.comments.application.commandservices.CommentCommandService;
import com.foundly.foundlyplatform.comments.domain.model.aggregates.Comment;
import com.foundly.foundlyplatform.comments.domain.model.commands.CreateCommentCommand;
import com.foundly.foundlyplatform.comments.domain.repositories.CommentRepository;
import com.foundly.foundlyplatform.shared.application.result.ApplicationError;
import com.foundly.foundlyplatform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Application service that handles comment creation commands.
 */
@Service
public class CommentCommandServiceImpl implements CommentCommandService {

    private static final int MAX_CONTENT_LENGTH = 1000;

    private final CommentRepository commentRepository;

    public CommentCommandServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Result<Comment, ApplicationError> handle(CreateCommentCommand command) {
        if (command.authorId() == null || command.targetUserId() == null) {
            return Result.failure(ApplicationError.validationError("comment", "Author and target user are required"));
        }

        var content = command.content() == null ? "" : command.content().trim();
        if (content.isEmpty()) {
            return Result.failure(ApplicationError.validationError("content", "Comment content must not be empty"));
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            return Result.failure(ApplicationError.validationError(
                    "content", "Comment content must not exceed %d characters".formatted(MAX_CONTENT_LENGTH)));
        }

        var comment = new Comment(command.authorId(), command.targetUserId(), content);
        var saved = commentRepository.save(comment);
        return Result.success(saved);
    }
}
