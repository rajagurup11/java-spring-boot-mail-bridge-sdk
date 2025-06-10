package io.github.rajagurup.mailbridge.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration for a remote email bridge client.
 */
@Validated
@ConfigurationProperties(prefix = "mail-bridge.remote")
public record EmailBridgeClientProperties(
        @NotBlank(message = "Remote email endpoint must not be blank") String endpoint,
        String authHeaderName,
        String authToken) {
}

