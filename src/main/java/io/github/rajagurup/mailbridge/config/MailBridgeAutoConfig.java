package io.github.rajagurup.mailbridge.config;

import io.github.rajagurup.mailbridge.service.EmailService;
import io.github.rajagurup.mailbridge.service.EmailServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

@Configuration
@ConditionalOnProperty(
        prefix = "mail-bridge",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true)
@EnableConfigurationProperties(EmailProperties.class)
@Generated
public class MailBridgeAutoConfig {

    @Bean
    @ConditionalOnMissingBean(EmailService.class)
    @ConditionalOnProperty(name = "mail-bridge.impl", havingValue = "local", matchIfMissing = true)
    public EmailService emailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        return new EmailServiceImpl(mailSender, templateEngine);
    }
}
