package io.github.rajagurup.mailbridge.service;

import io.github.rajagurup.mailbridge.exception.EmailSendingException;
import io.github.rajagurup.mailbridge.model.EmailRequest;
import io.github.rajagurup.mailbridge.model.EmailResponse;
import io.github.rajagurup.mailbridge.service.attachment.AttachmentProcessor;
import io.github.rajagurup.mailbridge.service.content.EmailContentResolver;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;

/**
 * Local implementation of EmailService that sends emails using a JavaMailSender.
 */
@Slf4j
@RequiredArgsConstructor
public final class LocalEmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailContentResolver contentResolver;
    private final AttachmentProcessor attachmentProcessor;

    @Override
    public EmailResponse sendEmail(EmailRequest request) throws EmailSendingException {
        log.debug("Sending email request: {}", request);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            setupRecipients(helper, request);
            helper.setSubject(request.subject());

            String content = contentResolver.resolveContent(request);
            boolean isHtml = contentResolver.isHtmlContent(request);

            helper.setText(content, isHtml);
            attachmentProcessor.addAttachments(helper, request.attachments());

            mailSender.send(mimeMessage);
            log.info("Email sent successfully to {}", request.to());

            return new EmailResponse(true, "Email sent successfully", content, request.to());
        } catch (Exception ex) {
            log.error("Failed to send email to {} with subject '{}'", request.to(), request.subject(), ex);
            throw new EmailSendingException("Email sending failed", ex);
        }
    }

    private void setupRecipients(MimeMessageHelper helper, EmailRequest request) throws MessagingException {
        helper.setFrom(request.from());
        helper.setTo(request.to().toArray(new String[0]));

        if (!CollectionUtils.isEmpty(request.cc())) {
            helper.setCc(request.cc().toArray(new String[0]));
        }
        if (!CollectionUtils.isEmpty(request.bcc())) {
            helper.setBcc(request.bcc().toArray(new String[0]));
        }
    }

}
