package org.example.documentservice.exception;

public class DocumentNotFoundException extends NotFoundException {
  public static final String DEFAULT_MESSAGE = "Документ не найден";

  public DocumentNotFoundException(String message) {
    super(message);
  }

  public DocumentNotFoundException() {
    this(DEFAULT_MESSAGE);
  }
}
