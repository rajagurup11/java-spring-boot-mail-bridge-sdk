package io.github.rajagurup.mailbridge.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class MailBridgeAutoConfigTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withPropertyValues(
              "mailbridge.host=smtp.example.com",
              "mailbridge.port=587",
              "mailbridge.username=testuser",
              "mailbridge.password=testpass",
              "mailbridge.from=no-reply@example.com",
              "mailbridge.enabled=true")
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
