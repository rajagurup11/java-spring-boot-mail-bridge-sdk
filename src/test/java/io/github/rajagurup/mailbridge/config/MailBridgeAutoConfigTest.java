package io.github.rajagurup.mailbridge.config;

import io.github.rajagurup.mailbridge.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

import static org.assertj.core.api.Assertions.assertThat;

class MailBridgeAutoConfigTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withPropertyValues(
                            "spring.mail.host=smtp.example.com",
                            "spring.mail.port=587",
                            "spring.mail.username=username",
                            "spring.mail.password=password")
                    .withUserConfiguration(MockDependencies.class)
                    .withUserConfiguration(MailBridgeAutoConfig.class);

    @Test
    void shouldLoadAutoConfig_whenEnabledTrueAndImplLocal() {
        contextRunner
                .withPropertyValues("mail-bridge.enabled=true", "mail-bridge.impl=local")
                .run(
                        context -> {
                            assertThat(context).hasSingleBean(EmailService.class);
                        });
    }

    @Test
    void shouldLoadAutoConfig_whenPropertiesAreMissingAndMatchIfMissingTrue() {
        contextRunner.run(
                context -> {
                    assertThat(context).hasSingleBean(EmailService.class);
                });
    }

    @Test
    void shouldNotLoadAutoConfig_whenEnabledFalse() {
        contextRunner
                .withPropertyValues("mail-bridge.enabled=false")
                .run(
                        context -> {
                            assertThat(context).doesNotHaveBean(EmailService.class);
                        });
    }

    @Test
    void shouldNotLoadEmailService_whenImplIsNotLocal() {
        contextRunner
                .withPropertyValues("mail-bridge.enabled=true", "mail-bridge.impl=remote") // not "local"
                .run(
                        context -> {
                            assertThat(context).doesNotHaveBean(EmailService.class);
                        });
    }

    @Configuration
    static class MockDependencies {

        @Bean
        public JavaMailSender mailSender() {
            return org.mockito.Mockito.mock(JavaMailSender.class);
        }

        @Bean
        public TemplateEngine templateEngine() {
            return org.mockito.Mockito.mock(TemplateEngine.class);
        }
    }
}
