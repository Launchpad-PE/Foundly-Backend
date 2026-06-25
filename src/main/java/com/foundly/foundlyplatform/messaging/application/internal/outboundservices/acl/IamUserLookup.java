package com.foundly.foundlyplatform.messaging.application.internal.outboundservices.acl;

import com.foundly.foundlyplatform.iam.application.queryservices.UserQueryService;
import com.foundly.foundlyplatform.iam.domain.model.aggregates.User;
import com.foundly.foundlyplatform.iam.domain.model.queries.GetUserByUsernameQuery;
import org.springframework.stereotype.Service;

/**
 * ACL of the messaging bounded context towards IAM.
 *
 * <p>Resolves the authenticated principal (a username carried in the JWT) into the
 * stable user identifier used by messaging, without leaking IAM internals.</p>
 */
@Service
public class IamUserLookup {

    private final UserQueryService userQueryService;

    public IamUserLookup(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    /**
     * Resolves a username into its user identifier.
     *
     * @param username the username (JWT subject)
     * @return the user id, or {@code null} when the user is not found
     */
    public Long findUserIdByUsername(String username) {
        if (username == null) return null;
        return userQueryService.handle(new GetUserByUsernameQuery(username))
                .map(User::getId)
                .orElse(null);
    }
}
