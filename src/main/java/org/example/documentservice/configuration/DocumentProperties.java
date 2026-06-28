package org.example.documentservice.configuration;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "document")
@Builder
public record DocumentProperties(
    String templateBucket,
    String documentBucket,
    String placeholderRegex
) {
}
