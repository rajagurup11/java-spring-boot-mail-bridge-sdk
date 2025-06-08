package io.github.rajagurup.mailbridge.exception;

import org.springframework.mail.MailSendException;

/** Exception thrown when an email cannot be sent. */
public class EmailSendingException extends MailSendException {

  public EmailSendingException(String message, Throwable cause) {
    super(message, cause);
  }
}
