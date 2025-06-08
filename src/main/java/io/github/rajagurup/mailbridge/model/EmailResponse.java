package io.github.rajagurup.mailbridge.model;

import java.util.List;
import lombok.Builder;

/**
 * Represents the result of sending an email.
 *
 * @param success Whether the operation was successful
 * @param messageId Optional ID for tracking or logs
 * @param message Description or error message
 * @param recipients List of primary recipients
 */
@Builder
public record EmailResponse(
    boolean success, String messageId, String message, List<String> recipients) {}
