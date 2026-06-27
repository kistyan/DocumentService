package org.example.documentservice.exception;

import lombok.Getter;

@Getter
public class UnknownPlaceholderTypeException extends IllegalArgumentException {
  private final String placeholderType;

  public UnknownPlaceholderTypeException(String placeholderType) {
    super("Неизвестный тип плейсхолдера: " + placeholderType);
    this.placeholderType = placeholderType;
  }
}
