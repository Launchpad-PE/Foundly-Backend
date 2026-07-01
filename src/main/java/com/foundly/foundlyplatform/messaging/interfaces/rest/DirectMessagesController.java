package com.foundly.foundlyplatform.messaging.interfaces.rest;

import com.foundly.foundlyplatform.messaging.application.commandservices.DirectMessageCommandService;
import com.foundly.foundlyplatform.messaging.application.internal.outboundservices.acl.IamUserLookup;
import com.foundly.foundlyplatform.messaging.application.queryservices.DirectMessageQueryService;
import com.foundly.foundlyplatform.messaging.domain.model.commands.SendDirectMessageCommand;
import com.foundly.foundlyplatform.messaging.domain.model.queries.GetConversationQuery;
import com.foundly.foundlyplatform.messaging.domain.model.queries.GetConversationsForUserQuery;
import com.foundly.foundlyplatform.messaging.interfaces.rest.resources.ConversationResource;
import com.foundly.foundlyplatform.messaging.interfaces.rest.resources.DirectMessageResource;
import com.foundly.foundlyplatform.messaging.interfaces.rest.resources.SendMessageResource;
import com.foundly.foundlyplatform.messaging.interfaces.rest.transform.DirectMessageResourceFromEntityAssembler;
import com.foundly.foundlyplatform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * REST controller for direct (1-to-1) messaging.
 *
 * <p>Sending persists the message and pushes it over WebSocket to both participants
 * ({@code /user/queue/messages}); reading returns conversation history and the
 * conversation list. The sender is always resolved from the authenticated JWT.</p>
 */
@RestController
@RequestMapping(value = "/api/v1/messages", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Messages", description = "Direct messaging (1-to-1) endpoints")
public class DirectMessagesController {

    private static final String USER_QUEUE_MESSAGES = "/queue/messages";

    private final DirectMessageCommandService commandService;
    private final DirectMessageQueryService queryService;
    private final IamUserLookup userLookup;
    private final SimpMessagingTemplate messagingTemplate;

    public DirectMessagesController(DirectMessageCommandService commandService,
                                    DirectMessageQueryService queryService,
                                    IamUserLookup userLookup,
                                    SimpMessagingTemplate messagingTemplate) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.userLookup = userLookup;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Sends a direct message to another user (sender taken from the JWT).
     * On success the message is pushed in real time to both participants.
     */
    @PostMapping
    @Operation(
            summary = "Send a direct message",
            description = "Persists the message and pushes it over WebSocket to the recipient (and the sender).",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageResource resource, Principal principal) {
        var senderId = currentUserId(principal);
        if (senderId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var command = new SendDirectMessageCommand(senderId, resource.recipientId(), resource.content());
        var result = commandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> {
            var dto = DirectMessageResourceFromEntityAssembler.toResourceFromEntity(message);
            messagingTemplate.convertAndSendToUser(String.valueOf(message.getRecipientId()), USER_QUEUE_MESSAGES, dto);
            messagingTemplate.convertAndSendToUser(String.valueOf(message.getSenderId()), USER_QUEUE_MESSAGES, dto);
            return dto;
        }, HttpStatus.CREATED);
    }

    /**
     * Returns the conversation (message history) between me and another user.
     */
    @GetMapping("/{otherUserId}")
    @Operation(
            summary = "Get the conversation with another user",
            description = "Returns all messages exchanged between the authenticated user and the given user, oldest first.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<?> getConversation(@PathVariable Long otherUserId, Principal principal) {
        var me = currentUserId(principal);
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var messages = queryService.handle(new GetConversationQuery(me, otherUserId)).stream()
                .map(DirectMessageResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(messages);
    }

    /**
     * Lists my conversations (the other participant + the last message of each).
     */
    @GetMapping("/conversations")
    @Operation(
            summary = "List my conversations",
            description = "Returns one entry per user I have exchanged messages with, including the last message.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<?> getConversations(Principal principal) {
        var me = currentUserId(principal);
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var byCounterpart = new LinkedHashMap<Long, ConversationResource>();
        for (var message : queryService.handle(new GetConversationsForUserQuery(me))) {
            var fromMe = message.getSenderId().equals(me);
            var otherUserId = fromMe ? message.getRecipientId() : message.getSenderId();
            byCounterpart.putIfAbsent(otherUserId, new ConversationResource(
                    otherUserId, message.getContent(), message.getCreatedAt(), fromMe));
        }
        return ResponseEntity.ok(List.copyOf(byCounterpart.values()));
    }

    private Long currentUserId(Principal principal) {
        return principal != null ? userLookup.findUserIdByUsername(principal.getName()) : null;
    }
}
