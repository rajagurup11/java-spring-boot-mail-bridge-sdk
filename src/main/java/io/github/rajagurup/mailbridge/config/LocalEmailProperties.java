package io.github.rajagurup.mailbridge.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for email settings.
 */
@Validated
@ConfigurationProperties(prefix = "spring.mail")
@Generated
public record LocalEmailProperties(@NotBlank String username,
                                   @NotBlank String password,
                                   @NotBlank String host,
                                   @NotNull Integer port) {
}
