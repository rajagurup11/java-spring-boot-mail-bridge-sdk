package io.github.rajagurup.mailbridge.model;

import java.util.Arrays;

/**
 * Enum for supported MIME types to avoid invalid or misspelled content types.
 */
public enum SupportedMimeType {
    PDF("application/pdf"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif"),
    TXT("text/plain"),
    HTML("text/html"),
    DOC("application/msword"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    XLS("application/vnd.ms-excel"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    CSV("text/csv"),
    JSON("application/json"),
    ZIP("application/zip");

    private final String mimeType;

    SupportedMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Check if a given MIME type string is supported.
     *
     * @param mimeType MIME type string to check
     * @return true if supported, false otherwise
     */
    public static boolean isSupported(String mimeType) {
        return Arrays.stream(values()).anyMatch(type -> type.mimeType.equalsIgnoreCase(mimeType));
    }

    /**
     * Optional: Get enum constant by MIME type string.
     *
     * @param mimeType MIME type string
     * @return SupportedMimeType enum or null if not found
     */
    public static SupportedMimeType fromMimeType(String mimeType) {
        return Arrays.stream(values())
                .filter(type -> type.mimeType.equalsIgnoreCase(mimeType))
                .findFirst()
                .orElse(null);
    }

    public String getMimeType() {
        return mimeType;
    }
}
