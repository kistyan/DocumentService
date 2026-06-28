package org.example.documentservice.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.documentservice.exception.MinioDownloadException;
import org.example.documentservice.exception.MinioUploadException;
import org.example.documentservice.service.MinioService;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
  private static final long DOWNLOAD_PART_SIZE = 5242880L; // минимальный размер части - 5MiB

  private final MinioClient minioClient;

  @Override
  public void upload(String bucketName, String fileName, InputStream inputStream) {
    try {
      minioClient.putObject(PutObjectArgs.builder()
          .bucket(bucketName)
          .object(fileName)
          .stream(inputStream, null, DOWNLOAD_PART_SIZE)
          .build());
    }
    catch (MinioException exception) {
      log.error("Ошибка записи файла", exception);
      throw new MinioUploadException();
    }
  }

  @Override
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
      log.error("Ошибка чтения файла", exception);
      throw new MinioDownloadException();
    }
    catch (MinioException exception) {
      log.error("Ошибка чтения файла", exception);
      throw new MinioDownloadException();
    }
  }
}
