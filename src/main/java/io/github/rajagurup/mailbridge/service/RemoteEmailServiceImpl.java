package io.github.rajagurup.mailbridge.service;

import io.github.rajagurup.mailbridge.config.EmailBridgeClientProperties;
import io.github.rajagurup.mailbridge.exception.EmailSendingException;
import io.github.rajagurup.mailbridge.model.EmailRequest;
import io.github.rajagurup.mailbridge.model.EmailResponse;
import io.github.rajagurup.mailbridge.service.content.EmailContentResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * Remote implementation of EmailService that calls an on-prem email service,
 * and prepares the email content (template or plain body) before sending.
 */
@Slf4j
@RequiredArgsConstructor
public final class RemoteEmailServiceImpl implements EmailService {

    private final RestTemplate restTemplate;
    private final EmailBridgeClientProperties properties;
    private final EmailContentResolver contentResolver;

    @Override
    public EmailResponse sendEmail(EmailRequest request) throws EmailSendingException {
        log.info("Calling remote email service at {}", properties.endpoint());

        try {
            // Resolve content from template or body
            String resolvedBody = contentResolver.resolveContent(request);
            boolean isHtml = contentResolver.isHtmlContent(request);

            // Create a new request with resolved body
            EmailRequest finalRequest = EmailRequest.builder()
                    .from(request.from())
                    .to(request.to())
                    .cc(request.cc())
                    .bcc(request.bcc())
                    .subject(request.subject())
                    .body(resolvedBody)
                    .template(request.template())
                    .model(request.model())
                    .sendAsHtml(isHtml)
                    .attachments(request.attachments())
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            if (properties.authHeaderName() != null && properties.authToken() != null) {
                headers.set(properties.authHeaderName(), properties.authToken());
            }

            HttpEntity<EmailRequest> entity = new HttpEntity<>(finalRequest, headers);
            ResponseEntity<EmailResponse> response = restTemplate.exchange(
                    properties.endpoint(),
                    HttpMethod.POST,
                    entity,
                    EmailResponse.class
            );

            log.info("Remote email service responded with status {}", response.getStatusCode());

            return new EmailResponse(true, "Email sent successfully", resolvedBody, request.to());
        } catch (HttpStatusCodeException ex) {
            String body = ex.getResponseBodyAsString();
            log.error("Remote service error: {} - {}", ex.getStatusCode(), body, ex);
            throw new EmailSendingException("Remote email service returned error: " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            log.error("Failed to call remote email service", ex);
            throw new EmailSendingException("Failed to call remote email service", ex);
        }
    }
}
