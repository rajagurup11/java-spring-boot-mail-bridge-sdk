package io.github.rajagurup.mailbridge.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/** Configuration properties for email settings. */
@Validated
@ConfigurationProperties(prefix = "mailbridge")
@Generated
public record EmailProperties(
    @NotBlank String username,
    @NotBlank String password,
    @NotBlank String from,
    @NotBlank String host,
    @NotNull Integer port) {}
