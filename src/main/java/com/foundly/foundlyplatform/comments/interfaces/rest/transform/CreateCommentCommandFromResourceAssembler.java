package com.foundly.foundlyplatform.comments.interfaces.rest.transform;

import com.foundly.foundlyplatform.comments.domain.model.commands.CreateCommentCommand;
import com.foundly.foundlyplatform.comments.interfaces.rest.resources.CreateCommentResource;

/**
 * Assembler that builds a {@link CreateCommentCommand} from the REST request resource.
 */
public class CreateCommentCommandFromResourceAssembler {

    /**
     * Builds the create-comment command, taking the target user from the request path.
     *
     * @param targetUserId identifier of the commented user (from the path)
     * @param resource     create-comment request body
     * @return the create-comment command
     */
    public static CreateCommentCommand toCommandFromResource(Long targetUserId, CreateCommentResource resource) {
        return new CreateCommentCommand(resource.authorId(), targetUserId, resource.content());
    }
}
