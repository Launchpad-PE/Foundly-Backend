package com.foundly.foundlyplatform.comments.application.commandservices;

import com.foundly.foundlyplatform.comments.domain.model.aggregates.Comment;
import com.foundly.foundlyplatform.comments.domain.model.commands.CreateCommentCommand;
import com.foundly.foundlyplatform.shared.application.result.ApplicationError;
import com.foundly.foundlyplatform.shared.application.result.Result;

/**
 * Application service contract for comment commands.
 */
public interface CommentCommandService {

    /**
     * Handles the creation of a comment on a user's profile.
     *
     * @param command create-comment command
     * @return created comment aggregate, or an application error
     */
    Result<Comment, ApplicationError> handle(CreateCommentCommand command);
}
