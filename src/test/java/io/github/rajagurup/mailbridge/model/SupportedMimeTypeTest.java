package io.github.rajagurup.mailbridge.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SupportedMimeTypeTest {

  @Test
  void isSupported_givenSupportedMimeType_shouldReturnTrue() {
    for (SupportedMimeType type : SupportedMimeType.values()) {
      assertThat(SupportedMimeType.isSupported(type.getMimeType())).isTrue();
      assertThat(SupportedMimeType.isSupported(type.getMimeType().toUpperCase())).isTrue();
    }
  }

  @Test
  void isSupported_givenUnsupportedMimeType_shouldReturnFalse() {
    assertThat(SupportedMimeType.isSupported("application/unknown")).isFalse();
    assertThat(SupportedMimeType.isSupported(null)).isFalse();
    assertThat(SupportedMimeType.isSupported("")).isFalse();
  }

  @Test
  void fromMimeType_givenValidMimeType_shouldReturnCorrectEnum() {
    assertThat(SupportedMimeType.fromMimeType("text/html")).isEqualTo(SupportedMimeType.HTML);
    assertThat(SupportedMimeType.fromMimeType("text/plain")).isEqualTo(SupportedMimeType.TXT);
    assertThat(SupportedMimeType.fromMimeType("application/json"))
        .isEqualTo(SupportedMimeType.JSON);
  }

  @Test
  void fromMimeType_givenValidMimeTypeWithDifferentCase_shouldReturnCorrectEnum() {
    assertThat(SupportedMimeType.fromMimeType("TEXT/HTML")).isEqualTo(SupportedMimeType.HTML);
    assertThat(SupportedMimeType.fromMimeType("Application/Json"))
        .isEqualTo(SupportedMimeType.JSON);
  }

  @Test
  void fromMimeType_givenUnknownMimeType_shouldReturnNull() {
    assertThat(SupportedMimeType.fromMimeType("image/pnx")).isNull();
    assertThat(SupportedMimeType.fromMimeType("invalid/type")).isNull();
  }

  @Test
  void fromMimeType_givenNullMimeType_shouldReturnNull() {
    assertThat(SupportedMimeType.fromMimeType(null)).isNull();
  }
}
