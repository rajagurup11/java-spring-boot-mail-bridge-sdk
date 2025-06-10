package io.github.rajagurup.mailbridge.service.attachment;

import io.github.rajagurup.mailbridge.model.Attachment;
import io.github.rajagurup.mailbridge.model.SupportedMimeType;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;

import java.util.Base64;
import java.util.List;

@Slf4j
public class AttachmentProcessor {

    public void addAttachments(MimeMessageHelper helper, List<Attachment> attachments)
            throws MessagingException {
        if (CollectionUtils.isEmpty(attachments)) {
            return;
        }

        log.debug("Adding {} attachments", attachments.size());

        for (Attachment attachment : attachments) {
            validate(attachment);

            byte[] decodedBytes = Base64.getDecoder().decode(attachment.base64Content());
            ByteArrayResource resource = new ByteArrayResource(decodedBytes);
            helper.addAttachment(attachment.filename(), resource, attachment.mimeType());
        }
    }

    private void validate(Attachment attachment) {
        if (attachment.mimeType() == null) {
            throw new IllegalArgumentException("Attachment MIME type cannot be null");
        }

        if (!SupportedMimeType.isSupported(attachment.mimeType())) {
            throw new IllegalArgumentException("Unsupported MIME type: " + attachment.mimeType());
        }
    }
}

