package org.example.documentservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "work-acceptance-act")
public record WorkAcceptanceActProperties(
    String listDelimiter,

    String templateName,

    String nameFormat
) {
}
