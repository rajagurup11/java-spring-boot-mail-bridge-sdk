package io.github.rajagurup.mailbridge.service.content;

import io.github.rajagurup.mailbridge.model.EmailRequest;

public interface EmailContentResolver {

    /**
     * Resolves email content (HTML or plain text) based on the request.
     */
    String resolveContent(EmailRequest request);

    /**
     * Determines if the content should be sent as HTML.
     */
    boolean isHtmlContent(EmailRequest request);
}
