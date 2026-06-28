package org.example.documentservice.exception;

public class MinioDownloadException extends InternalServerErrorException {
  public static final String DEFAULT_MESSAGE = "Ошибка чтения файла";

  public MinioDownloadException(String message) {
    super(message);
  }

  public MinioDownloadException() {
    this(DEFAULT_MESSAGE);
  }
}
