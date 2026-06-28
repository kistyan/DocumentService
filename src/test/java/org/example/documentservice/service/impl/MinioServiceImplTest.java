package org.example.documentservice.service.impl;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import io.minio.messages.ErrorResponse;
import org.example.documentservice.exception.MinioDownloadException;
import org.example.documentservice.exception.MinioUploadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MinioServiceImplTest {
  private static final String BUCKET_NAME = "bucket";
  private static final String FILE_NAME = "file.docx";

  @Mock
  private MinioClient minioClient;

  @InjectMocks
  private MinioServiceImpl minioServiceImpl;

  @Test
  void upload_shouldCallMinioClient() throws MinioException {
    InputStream inputStream = new ByteArrayInputStream(new byte[0]);

    minioServiceImpl.upload(BUCKET_NAME, FILE_NAME, inputStream);

    verify(minioClient).putObject(any(PutObjectArgs.class));
  }

  @Test
  void upload_shouldThrowMinioUploadException() throws MinioException {
    InputStream inputStream = new ByteArrayInputStream(new byte[0]);

    when(minioClient.putObject(any(PutObjectArgs.class)))
        .thenThrow(new MinioException(""));

    assertThrows(MinioUploadException.class,
        () -> minioServiceImpl.upload(BUCKET_NAME, FILE_NAME, inputStream));
  }

  @Test
  void download_shouldReturnInputStream() throws MinioException {
    GetObjectResponse minioResponse = mock(GetObjectResponse.class);

    when(minioClient.getObject(any(GetObjectArgs.class)))
        .thenReturn(minioResponse);

    Optional<InputStream> result = minioServiceImpl.download(BUCKET_NAME, FILE_NAME);

    assertTrue(result.isPresent());
  }

  @Test
  void download_shouldReturnEmpty_whenNoSuchKey() throws Exception {
    ErrorResponseException exception = mock(ErrorResponseException.class);
    ErrorResponse errorResponse = mock(ErrorResponse.class);

    when(exception.errorResponse()).thenReturn(errorResponse);
    when(errorResponse.code()).thenReturn("NoSuchKey");

    when(minioClient.getObject(any(GetObjectArgs.class)))
        .thenThrow(exception);

    Optional<InputStream> result = minioServiceImpl.download(BUCKET_NAME, FILE_NAME);

    assertTrue(result.isEmpty());
  }

  @Test
  void download_shouldThrowMinioDownloadException_onErrorResponse() throws Exception {
    ErrorResponseException exception = mock(ErrorResponseException.class);
    ErrorResponse errorResponse = mock(ErrorResponse.class);

    when(exception.errorResponse()).thenReturn(errorResponse);
    when(errorResponse.code()).thenReturn("");

    when(minioClient.getObject(any(GetObjectArgs.class)))
        .thenThrow(exception);

    assertThrows(MinioDownloadException.class,
        () -> minioServiceImpl.download(BUCKET_NAME, FILE_NAME));
  }

  @Test
  void download_shouldThrowMinioDownloadException_onMinioException() throws Exception {
    when(minioClient.getObject(any(GetObjectArgs.class)))
        .thenThrow(new MinioException(""));

    assertThrows(MinioDownloadException.class,
        () -> minioServiceImpl.download(BUCKET_NAME, FILE_NAME));
  }
}