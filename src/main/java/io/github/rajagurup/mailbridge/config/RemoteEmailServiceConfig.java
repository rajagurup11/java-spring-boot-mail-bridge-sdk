package io.github.rajagurup.mailbridge.config;

import io.github.rajagurup.mailbridge.service.EmailService;
import io.github.rajagurup.mailbridge.service.RemoteEmailServiceImpl;
import io.github.rajagurup.mailbridge.service.content.EmailContentResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for the remote email service.
 */
@Configuration
@ConditionalOnProperty(name = "mail-bridge.impl", havingValue = "remote")
@EnableConfigurationProperties(EmailBridgeClientProperties.class)
public class RemoteEmailServiceConfig {

    @Bean(name = "mailBridgeRestTemplate")
    @ConditionalOnMissingBean(name = "mailBridgeRestTemplate")
    public RestTemplate mailBridgeRestTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "mailBridgeRemoteEmailService")
    @ConditionalOnMissingBean(name = "mailBridgeRemoteEmailService")
    public EmailService mailBridgeRemoteEmailService(RestTemplate restTemplate,
                                                     EmailBridgeClientProperties properties,
                                                     EmailContentResolver contentResolver) {
        return new RemoteEmailServiceImpl(restTemplate, properties, contentResolver);
    }
}
