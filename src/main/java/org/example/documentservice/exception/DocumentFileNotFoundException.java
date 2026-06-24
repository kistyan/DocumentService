package org.example.documentservice.exception;

public class DocumentFileNotFoundException extends RuntimeException {
  public static final String DEFAULT_MESSAGE = "Файл документа не найден";

  public DocumentFileNotFoundException(String message) {
    super(message);
  }

  public DocumentFileNotFoundException() {
    this(DEFAULT_MESSAGE);
  }
}
