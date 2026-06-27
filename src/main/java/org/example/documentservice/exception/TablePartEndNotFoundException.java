package org.example.documentservice.exception;

public class TablePartEndNotFoundException extends IllegalArgumentException {
  public TablePartEndNotFoundException() {
    super("Не найден конец части таблицы");
  }
}
