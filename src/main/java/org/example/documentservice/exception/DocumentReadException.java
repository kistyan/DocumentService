package org.example.documentservice.exception;

public class DocumentReadException extends InternalServerErrorException {
  public static final String DEFAULT_MESSAGE = "Ошибка чтения документа";

  public DocumentReadException(String message) {
    super(message);
  }

  public DocumentReadException() {
    this(DEFAULT_MESSAGE);
  }
}
