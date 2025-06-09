package io.github.rajagurup.mailbridge.service;

import io.github.rajagurup.mailbridge.exception.EmailSendingException;
import io.github.rajagurup.mailbridge.model.EmailRequest;
import io.github.rajagurup.mailbridge.model.EmailResponse;

/**
 * Service for sending emails.
 */
public sealed interface EmailService permits EmailServiceImpl {

    /**
     * Sends an email.
     *
     * @param request email request object
     * @throws EmailSendingException if sending fails
     */
    EmailResponse sendEmail(EmailRequest request) throws EmailSendingException;
}
