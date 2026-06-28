package org.example.documentservice.configuration;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "work-acceptance-act")
@Builder
public record WorkAcceptanceActProperties(
    String listDelimiter,

    String templateName,

    String nameFormat
) {
}
