package org.example.documentservice.exception;

public class TemplateFileNotFoundException extends RuntimeException {
  public static final String DEFAULT_MESSAGE = "Файл шаблона не найден";

  public TemplateFileNotFoundException(String message) {
    super(message);
  }

  public TemplateFileNotFoundException() {
    this(DEFAULT_MESSAGE);
  }
}
