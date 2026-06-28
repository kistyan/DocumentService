package org.example.documentservice.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.documentservice.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  private ResponseEntity<String> handleException(Exception exception, HttpStatus status) {
    log.warn("Ошибка обработки запроса", exception);
    return ResponseEntity
        .status(status)
        .body(exception.getMessage());
  }

  @ExceptionHandler({
      BaseException.class
  })
  public ResponseEntity<String> handleBaseException(BaseException exception) {
    return handleException(exception, exception.getHttpStatus());
  }

  @ExceptionHandler({
      Exception.class
  })
  public ResponseEntity<String> handleInternalServerError(Exception exception) {
    return handleException(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
