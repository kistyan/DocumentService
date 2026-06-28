package org.example.documentservice.exception;

public class DocumentWriteException extends InternalServerErrorException {
  public static final String DEFAULT_MESSAGE = "Ошибка записи документа";

  public DocumentWriteException(String message) {
    super(message);
  }

  public DocumentWriteException() {
    this(DEFAULT_MESSAGE);
  }
}
