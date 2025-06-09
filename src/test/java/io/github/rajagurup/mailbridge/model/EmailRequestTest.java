package io.github.rajagurup.mailbridge.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailRequestTest {

    @Test
    void isBodyBased_shouldReturnFalse_whenTemplateIsPresent() {
        EmailRequest request =
                EmailRequest.builder().template("someTemplate.html").body("This is body").build();

        assertThat(request.isBodyBased()).isFalse();
    }

    @Test
    void isBodyBased_shouldReturnFalse_whenBodyIsNull() {
        EmailRequest request = EmailRequest.builder().template(null).body(null).build();

        assertThat(request.isBodyBased()).isFalse();
    }

    @Test
    void isBodyBased_shouldReturnFalse_whenBodyIsEmpty() {
        EmailRequest request = EmailRequest.builder().template(null).body("").build();

        assertThat(request.isBodyBased()).isFalse();
    }

    @Test
    void isBodyBased_shouldReturnFalse_whenBodyIsWhitespace() {
        EmailRequest request = EmailRequest.builder().template(null).body("   ").build();

        assertThat(request.isBodyBased()).isFalse();
    }
}
