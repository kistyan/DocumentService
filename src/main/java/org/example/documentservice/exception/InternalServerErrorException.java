package org.example.documentservice.exception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends BaseException {
  public InternalServerErrorException(String message) {
    super(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
