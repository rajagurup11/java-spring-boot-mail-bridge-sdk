package io.github.rajagurup.mailbridge.config;

import io.github.rajagurup.mailbridge.service.EmailService;
import io.github.rajagurup.mailbridge.service.LocalEmailServiceImpl;
import io.github.rajagurup.mailbridge.service.attachment.AttachmentProcessor;
import io.github.rajagurup.mailbridge.service.content.EmailContentResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Configuration for the local email service.
 */
@Configuration
@ConditionalOnProperty(name = "mail-bridge.impl", havingValue = "local")
@EnableConfigurationProperties(LocalEmailProperties.class)
public class LocalEmailServiceConfig {

    @Bean(name = "mailBridgeLocalEmailService")
    @ConditionalOnMissingBean(name = "mailBridgeLocalEmailService")
    public EmailService mailBridgeLocalEmailService(JavaMailSender mailSender,
                                                    EmailContentResolver contentResolver,
                                                    AttachmentProcessor attachmentProcessor) {
        return new LocalEmailServiceImpl(mailSender, contentResolver, attachmentProcessor);
    }
}