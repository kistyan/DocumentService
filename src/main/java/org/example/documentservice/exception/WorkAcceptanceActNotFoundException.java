package org.example.documentservice.exception;

public class WorkAcceptanceActNotFoundException extends NotFoundException {
  public static final String DEFAULT_MESSAGE = "Акт сдачи-приёмки работ не найден";

  public WorkAcceptanceActNotFoundException(String message) {
    super(message);
  }

  public WorkAcceptanceActNotFoundException() {
    this(DEFAULT_MESSAGE);
  }
}
