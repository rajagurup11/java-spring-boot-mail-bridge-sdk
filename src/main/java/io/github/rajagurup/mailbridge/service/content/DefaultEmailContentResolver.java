package io.github.rajagurup.mailbridge.service.content;

import io.github.rajagurup.mailbridge.model.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Default implementation of EmailContentResolver that uses Thymeleaf to resolve the email content.
 */
@RequiredArgsConstructor
public class DefaultEmailContentResolver implements EmailContentResolver {

    private final TemplateEngine templateEngine;

    @Override
    public String resolveContent(EmailRequest request) {
        if (request.isTemplateBased()) {
            Context context = new Context();
            context.setVariables(request.model());
            return templateEngine.process(request.template(), context);
        } else if (request.isBodyBased()) {
            return request.body();
        } else {
            throw new IllegalArgumentException("Either template or body must be provided.");
        }
    }

    @Override
    public boolean isHtmlContent(EmailRequest request) {
        return request.isTemplateBased() || request.sendAsHtml();
    }
}
