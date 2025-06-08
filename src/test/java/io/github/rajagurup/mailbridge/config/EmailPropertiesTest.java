package io.github.rajagurup.mailbridge.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class EmailPropertiesTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner().withUserConfiguration(TestConfig.class);

  @Test
  void shouldBindPropertiesSuccessfully() {
    contextRunner
        .withPropertyValues(
            "spring.mail.username=testuser",
            "spring.mail.password=testpass",
            "spring.mail.host=smtp.example.com",
            "spring.mail.port=587")
        .run(
            context -> {
              assertThat(context).hasSingleBean(EmailProperties.class);
              EmailProperties props = context.getBean(EmailProperties.class);
              assertThat(props.username()).isEqualTo("testuser");
              assertThat(props.password()).isEqualTo("testpass");
              assertThat(props.host()).isEqualTo("smtp.example.com");
              assertThat(props.port()).isEqualTo(587);
            });
  }

  @Test
  void shouldFailBinding_whenRequiredPropertiesMissing() {
    contextRunner
        // no properties set, so required fields are missing
        .run(
        context -> {
          assertThat(context).hasFailed();
          assertThat(context.getStartupFailure())
              .isInstanceOf(ConfigurationPropertiesBindException.class);
        });
  }

  @EnableConfigurationProperties(EmailProperties.class)
  static class TestConfig {}
}
