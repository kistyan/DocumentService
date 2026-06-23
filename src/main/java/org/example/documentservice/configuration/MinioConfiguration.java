package org.example.documentservice.configuration;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MinioConfiguration {
  private final MinioProperties minioProperties;

  @Bean
  public MinioClient minioClient(MinioProperties properties) {
    return MinioClient.builder()
        .endpoint(properties.endpoint())
        .credentials(
            properties.accessKey(),
            properties.secretKey()
        )
        .build();
  }
}
