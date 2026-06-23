package org.example.documentservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "document")
public record DocumentProperties(
    String templateBucket,
    String documentBucket,
    String placeholderRegex
) {
}
