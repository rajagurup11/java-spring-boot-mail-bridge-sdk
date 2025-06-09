package io.github.rajagurup.mailbridge.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Represents the email request to be sent.
 *
 * @param to          List of primary recipients (must not be null or empty)
 * @param cc          Optional list of CC recipients
 * @param bcc         Optional list of BCC recipients
 * @param subject     Subject of the email
 * @param template    Optional template file name (e.g., welcome.html)
 * @param body        Optional raw email content (ignored if template is provided)
 * @param sendAsHtml  Whether the raw body should be sent as HTML (ignored if using template)
 * @param model       Variables used in the template engine (ignored if no template)
 * @param attachments Optional list of file attachments
 */
@Builder
public record EmailRequest(
        @NotNull List<String> to,
        @NotNull String from,
        List<String> cc,
        List<String> bcc,
        @NotNull String subject,
        String template,
        String body,
        boolean sendAsHtml,
        Map<String, Object> model,
        List<Attachment> attachments) {
    /**
     * Checks if the email is template-based.
     *
     * @return true if a non-empty template is present
     */
    public boolean isTemplateBased() {
        return StringUtils.hasText(template);
    }

    /**
     * Checks if the email is raw-body-based.
     *
     * @return true if no template is used and a non-empty body is present
     */
    public boolean isBodyBased() {
        return !isTemplateBased() && StringUtils.hasText(body);
    }
}
