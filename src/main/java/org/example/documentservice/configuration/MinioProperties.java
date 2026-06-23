package org.example.documentservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record MinioProperties(
    String endpoint,
    String accessKey,
    String secretKey
) {
}
