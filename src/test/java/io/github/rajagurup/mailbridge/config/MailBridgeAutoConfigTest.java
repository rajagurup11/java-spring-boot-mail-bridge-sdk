package io.github.rajagurup.mailbridge.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class MailBridgeAutoConfigTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withPropertyValues(
              "spring.mail.host=smtp.example.com",
              "spring.mail.port=587",
              "spring.mail.username=testuser",
              "spring.mail.password=testpass")
          .withUserConfiguration(MailBridgeAutoConfig.class);

  @Test
  void shouldLoadAutoConfig_whenPropertyEnabledTrue() {
    contextRunner
        .withPropertyValues("mailbridge.enabled=true")
        .run(context -> assertThat(context).hasSingleBean(MailBridgeAutoConfig.class));
  }

  @Test
  void shouldLoadAutoConfig_whenPropertyMissingAndMatchIfMissingTrue() {
    contextRunner.run(context -> assertThat(context).hasSingleBean(MailBridgeAutoConfig.class));
  }

  @Test
  void shouldNotLoadAutoConfig_whenPropertyEnabledFalse() {
    contextRunner
        .withPropertyValues("mailbridge.enabled=false")
        .run(context -> assertThat(context).doesNotHaveBean(MailBridgeAutoConfig.class));
  }
}
