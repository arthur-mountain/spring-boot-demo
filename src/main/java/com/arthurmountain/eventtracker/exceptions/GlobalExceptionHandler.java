package com.arthurmountain.eventtracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
  // 400 - Bad Request
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
    return new ResponseEntity<>(new ErrorResponse("Bad Request: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  // 404 - Resource Not Found
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    return new ResponseEntity<>(new ErrorResponse("Not found: " + ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  // 500 - Internal Server Error
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleInternalServerException(Exception ex, WebRequest request) {
    return new ResponseEntity<>(
        new ErrorResponse("An unexpected error occurred, please contact administrator."),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
