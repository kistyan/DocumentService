package org.example.documentservice.exception;

public class UnexpectedTablePartEndException extends IllegalArgumentException {
  public UnexpectedTablePartEndException() {
    super("Неожиданный конец части таблицы");
  }
}
