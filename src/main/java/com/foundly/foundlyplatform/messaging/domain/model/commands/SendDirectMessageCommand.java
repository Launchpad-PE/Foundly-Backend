package com.foundly.foundlyplatform.messaging.domain.model.commands;

/**
 * Command to send a direct message from one user to another.
 *
 * @param senderId    identifier of the user sending the message (taken from the JWT)
 * @param recipientId identifier of the user receiving the message
 * @param content     the message text
 */
public record SendDirectMessageCommand(Long senderId, Long recipientId, String content) {
}
