package org.example.documentservice.exception;

public class MinioUploadException extends InternalServerErrorException {
  public static final String DEFAULT_MESSAGE = "Ошибка записи файла";

  public MinioUploadException(String message) {
    super(message);
  }

  public MinioUploadException() {
    this(DEFAULT_MESSAGE);
  }
}
