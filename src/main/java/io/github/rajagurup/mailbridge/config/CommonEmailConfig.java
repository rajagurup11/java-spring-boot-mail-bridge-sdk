package io.github.rajagurup.mailbridge.config;

import io.github.rajagurup.mailbridge.service.attachment.AttachmentProcessor;
import io.github.rajagurup.mailbridge.service.content.DefaultEmailContentResolver;
import io.github.rajagurup.mailbridge.service.content.EmailContentResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;

/**
 * Common configuration for email services
 */
@Configuration
public class CommonEmailConfig {

    @Bean
    @ConditionalOnMissingBean
    public EmailContentResolver emailContentResolver(TemplateEngine templateEngine) {
        return new DefaultEmailContentResolver(templateEngine);
    }

    @Bean
    @ConditionalOnMissingBean
    public AttachmentProcessor attachmentProcessor() {
        return new AttachmentProcessor();
    }
}
