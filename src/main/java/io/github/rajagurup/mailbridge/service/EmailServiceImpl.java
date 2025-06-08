package io.github.rajagurup.mailbridge.service;

import io.github.rajagurup.mailbridge.exception.EmailSendingException;
import io.github.rajagurup.mailbridge.model.Attachment;
import io.github.rajagurup.mailbridge.model.EmailRequest;
import io.github.rajagurup.mailbridge.model.EmailResponse;
import io.github.rajagurup.mailbridge.model.SupportedMimeType;
import jakarta.mail.internet.MimeMessage;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/** Implementation of EmailService using Spring JavaMailSender and Thymeleaf. */
@Slf4j
@Service
@RequiredArgsConstructor
public non-sealed class EmailServiceImpl implements EmailService {

  private final String TEMPLATE_BASE_PATH = "classpath:/templates/";
  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  /**
   * Sends an email using the provided EmailRequest.
   *
   * @param request the email request with details
   * @return EmailResponse
   * @throws EmailSendingException if sending fails
   */
  @Override
  public EmailResponse sendEmail(EmailRequest request) throws EmailSendingException {
    log.debug("sendEmail called with request: {}", request);

    try {
      log.info("Preparing to send email to: {}", request.to());

      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

      helper.setFrom(request.from());
      helper.setTo(request.to().toArray(new String[0]));
      if (!CollectionUtils.isEmpty(request.cc())) {
        helper.setCc(request.cc().toArray(new String[0]));
      }
      if (!CollectionUtils.isEmpty(request.bcc())) {
        helper.setBcc(request.bcc().toArray(new String[0]));
      }

      helper.setSubject(request.subject());

      String content;
      boolean isHtml;

      if (request.isTemplateBased()) {
        Context context = new Context();
        context.setVariables(request.model());
        content = templateEngine.process(TEMPLATE_BASE_PATH + request.template(), context);
        isHtml = true;
      } else if (request.isBodyBased()) {
        content = request.body();
        isHtml = request.sendAsHtml();
      } else {
        throw new IllegalArgumentException("Either template or body must be provided.");
      }

      helper.setText(content, isHtml);

      if (request.attachments() != null) {
        log.debug("Email contains {} attachments", request.attachments().size());

        for (Attachment attachment : request.attachments()) {
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
}
