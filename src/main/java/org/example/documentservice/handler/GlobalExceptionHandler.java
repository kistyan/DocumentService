package org.example.documentservice.handler;

import org.example.documentservice.exception.TemplateFileNotFoundException;
import org.example.documentservice.exception.UnknownTemplateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler({
      UnknownTemplateException.class,
      TemplateFileNotFoundException.class
  })
  public ResponseEntity<String> handleNotFound(Exception exception) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(exception.getMessage());
  }
}
