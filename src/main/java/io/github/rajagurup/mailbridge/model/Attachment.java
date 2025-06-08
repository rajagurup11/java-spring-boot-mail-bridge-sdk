package io.github.rajagurup.mailbridge.model;

import lombok.Builder;

/**
 * Represents an attachment for an email.
 *
 * @param filename Name of the file as it should appear in the email
 * @param mimeType MIME type (e.g., application/pdf)
 * @param base64Content Content of the file as a Base64-encoded string
 */
@Builder
public record Attachment(String filename, String mimeType, String base64Content) {}
