package org.example.documentservice.repository;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MinioRepository {
  private final MinioClient minioClient;

  public void upload(String bucketName, String fileName, InputStream inputStream) {
    try {
      minioClient.putObject(PutObjectArgs.builder()
          .bucket(bucketName)
          .object(fileName)
          .stream(inputStream, null, 5242880L) // минимальный размер части - 5MiB
          .build());
    }
    catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  public Optional<InputStream> download(String bucketName, String fileName) {
    try {
      return Optional.of(minioClient.getObject(
          GetObjectArgs.builder()
              .bucket(bucketName)
              .object(fileName)
              .build()
      ));
    }
    catch (ErrorResponseException exception) {
      if ("NoSuchKey".equals(exception.errorResponse().code())) {
        return Optional.empty();
      }
      throw new RuntimeException(exception);
    }
    catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }
}
