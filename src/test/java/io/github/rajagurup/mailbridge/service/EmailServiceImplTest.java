package io.github.rajagurup.mailbridge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import io.github.rajagurup.mailbridge.exception.EmailSendingException;
import io.github.rajagurup.mailbridge.model.Attachment;
import io.github.rajagurup.mailbridge.model.EmailRequest;
import io.github.rajagurup.mailbridge.model.EmailResponse;
import jakarta.mail.internet.MimeMessage;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

  @Mock private JavaMailSender mailSender;

  @Mock private TemplateEngine templateEngine;

  @Mock private MimeMessage mimeMessage;

  @InjectMocks private EmailServiceImpl emailService;

  @BeforeEach
  void setup() {
    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
  }

  @Test
  void sendEmail_withTemplate_success() throws Exception {
    EmailRequest request =
        EmailRequest.builder()
            .to(List.of("to@example.com"))
            .from("no-reply@example.com")
            .cc(List.of("cc@example.com"))
            .bcc(List.of("bcc@example.com"))
            .subject("Test Subject")
            .template("welcome.html")
            .model(Map.of("name", "Tester"))
            .build();

    when(templateEngine.process(eq("welcome.html"), any())).thenReturn("<h1>Hello Tester</h1>");

    EmailResponse response = emailService.sendEmail(request);

    verify(mailSender).send(mimeMessage);
    assertThat(response.success()).isTrue();
    assertThat(response.messageId()).isEqualTo("Email sent successfully");
    assertThat(response.message()).isEqualTo("<h1>Hello Tester</h1>");
    assertThat(response.recipients()).containsExactly("to@example.com");
  }

  @Test
  void sendEmail_withBodyBasedPlainText_success() throws Exception {
    EmailRequest request =
        EmailRequest.builder()
            .to(List.of("to@example.com"))
            .from("no-reply@example.com")
            .subject("Plain Text")
            .body("This is a plain text email")
            .sendAsHtml(false)
            .build();

    EmailResponse response = emailService.sendEmail(request);

    verify(mailSender).send(mimeMessage);
    assertThat(response.success()).isTrue();
    assertThat(response.messageId()).isEqualTo("Email sent successfully");
    assertThat(response.message()).isEqualTo("This is a plain text email");
    assertThat(response.recipients()).containsExactly("to@example.com");
  }

  @Test
  void sendEmail_withBodyBasedHtml_success() throws Exception {
    EmailRequest request =
        EmailRequest.builder()
            .to(List.of("to@example.com"))
            .from("no-reply@example.com")
            .subject("HTML Email")
            .body("<p>This is HTML</p>")
            .sendAsHtml(true)
            .build();

    EmailResponse response = emailService.sendEmail(request);

    verify(mailSender).send(mimeMessage);
    assertThat(response.success()).isTrue();
    assertThat(response.messageId()).isEqualTo("Email sent successfully");
    assertThat(response.message()).isEqualTo("<p>This is HTML</p>");
    assertThat(response.recipients()).containsExactly("to@example.com");
  }

  @Test
  void sendEmail_withAttachments_success() throws Exception {
    String base64Content = Base64.getEncoder().encodeToString("file content".getBytes());

    Attachment attachment =
        Attachment.builder()
            .filename("file.txt")
            .base64Content(base64Content)
            .mimeType("text/plain")
            .build();

    EmailRequest request =
        EmailRequest.builder()
            .to(List.of("to@example.com"))
            .from("no-reply@example.com")
            .subject("Email with attachment")
            .body("Please see attachment")
            .sendAsHtml(false)
            .attachments(List.of(attachment))
            .build();

    EmailResponse response = emailService.sendEmail(request);

    verify(mailSender).send(mimeMessage);
    assertThat(response.success()).isTrue();
    assertThat(response.messageId()).isEqualTo("Email sent successfully");
    assertThat(response.message()).isEqualTo("Please see attachment");
  }

  @Test
  void sendEmail_missingTemplateAndBody_throwsException() {
    EmailRequest request =
        EmailRequest.builder()
            .to(List.of("to@example.com"))
            .from("no-reply@example.com")
            .subject("Missing content")
            .build();

    assertThatThrownBy(() -> emailService.sendEmail(request))
        .isInstanceOf(EmailSendingException.class)
        .hasMessageContaining("Email sending failed")
        .cause()
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Either template or body must be provided");
  }

  @Test
  void sendEmail_nullAttachmentMimeType_throwsException() {
    Attachment attachment =
        Attachment.builder().filename("file.txt").base64Content("abc").mimeType(null).build();

    EmailRequest request =
        EmailRequest.builder()
            .to(List.of("to@example.com"))
            .from("no-reply@example.com")
            .subject("Null MIME")
            .body("Body")
            .sendAsHtml(false)
            .attachments(List.of(attachment))
            .build();

    assertThatThrownBy(() -> emailService.sendEmail(request))
        .isInstanceOf(EmailSendingException.class)
        .hasMessageContaining("Email sending failed")
        .cause()
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("MIME type cannot be null");
  }

  @Test
  void sendEmail_unsupportedMimeType_throwsException() {
    Attachment attachment =
        Attachment.builder()
            .filename("file.txt")
            .base64Content("abc")
            .mimeType("application/unsupported")
            .build();

    EmailRequest request =
        EmailRequest.builder()
            .to(List.of("to@example.com"))
            .from("no-reply@example.com")
            .subject("Unsupported MIME")
            .body("Body")
            .sendAsHtml(false)
            .attachments(List.of(attachment))
            .build();

    assertThatThrownBy(() -> emailService.sendEmail(request))
        .isInstanceOf(EmailSendingException.class)
        .hasMessageContaining("Email sending failed")
        .cause()
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Unsupported MIME type: application/unsupported");
  }

  @Test
  void sendEmail_mailSenderThrowsEmailSendingException() throws Exception {
    EmailRequest request =
        EmailRequest.builder()
            .to(List.of("to@example.com"))
            .from("no-reply@example.com")
            .subject("Test")
            .body("Body")
            .sendAsHtml(false)
            .build();

    doThrow(new RuntimeException("Mail server down")).when(mailSender).send(mimeMessage);

    assertThatThrownBy(() -> emailService.sendEmail(request))
        .isInstanceOf(EmailSendingException.class)
        .hasMessageContaining("Email sending failed");
  }
}
