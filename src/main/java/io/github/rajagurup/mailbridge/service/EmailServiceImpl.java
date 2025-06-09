package io.github.rajagurup.mailbridge.service;

import io.github.rajagurup.mailbridge.exception.EmailSendingException;
import io.github.rajagurup.mailbridge.model.Attachment;
import io.github.rajagurup.mailbridge.model.EmailRequest;
import io.github.rajagurup.mailbridge.model.EmailResponse;
import io.github.rajagurup.mailbridge.model.SupportedMimeType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Base64;
import java.util.List;

/**
 * Implementation of EmailService using Spring JavaMailSender and Thymeleaf.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public non-sealed class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    /**
     * Sends an email based on the provided request.
     *
     * @param request The EmailRequest containing the email details.
     * @return An EmailResponse indicating the success or failure of the email sending.
     * @throws EmailSendingException If the email sending fails.
     */
    @Override
    public EmailResponse sendEmail(EmailRequest request) throws EmailSendingException {
        log.debug("sendEmail called with request: {}", request);

        try {
            log.info("Preparing to send email to: {}", request.to());

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            setupRecipients(helper, request);
            helper.setSubject(request.subject());

            String content = resolveEmailContent(request);
            boolean isHtml = isHtmlContent(request);

            helper.setText(content, isHtml);
            addAttachments(helper, request.attachments());

            mailSender.send(mimeMessage);
            log.info("Email sent successfully to {}", request.to());

            return new EmailResponse(true, "Email sent successfully", content, request.to());
        } catch (Exception exception) {
            log.error(
                    "Failed to send email to {} with subject '{}'",
                    request.to(),
                    request.subject(),
                    exception);
            throw new EmailSendingException("Email sending failed", exception);
        }
    }

    private void setupRecipients(MimeMessageHelper helper, EmailRequest request)
            throws MessagingException {
        helper.setFrom(request.from());
        helper.setTo(request.to().toArray(new String[0]));

        if (!CollectionUtils.isEmpty(request.cc())) {
            helper.setCc(request.cc().toArray(new String[0]));
        }
        if (!CollectionUtils.isEmpty(request.bcc())) {
            helper.setBcc(request.bcc().toArray(new String[0]));
        }
    }

    private void addAttachments(MimeMessageHelper helper, List<Attachment> attachments)
            throws MessagingException {
        if (CollectionUtils.isEmpty(attachments)) {
            return;
        }

        log.debug("Email contains {} attachments", attachments.size());

        for (Attachment attachment : attachments) {
            if (attachment.mimeType() == null) {
                throw new IllegalArgumentException("Attachment MIME type cannot be null");
            }

            if (!SupportedMimeType.isSupported(attachment.mimeType())) {
                throw new IllegalArgumentException("Unsupported MIME type: " + attachment.mimeType());
            }

            byte[] decodedBytes = Base64.getDecoder().decode(attachment.base64Content());
            ByteArrayResource resource = new ByteArrayResource(decodedBytes);
            helper.addAttachment(attachment.filename(), resource, attachment.mimeType());
        }
    }

    private String resolveEmailContent(EmailRequest request) {
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

    private boolean isHtmlContent(EmailRequest request) {
        return request.isTemplateBased() || request.sendAsHtml();
    }
}
