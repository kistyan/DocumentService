package org.example.documentservice.exception;

public class UnknownTemplateException extends RuntimeException {
  public static final String DEFAULT_MESSAGE = "Неизвестный шаблон";

  public UnknownTemplateException(String message) {
    super(message);
  }

  public UnknownTemplateException() {
    this(DEFAULT_MESSAGE);
  }
}
