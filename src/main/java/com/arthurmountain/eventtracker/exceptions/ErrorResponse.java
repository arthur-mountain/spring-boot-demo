package com.arthurmountain.eventtracker.exceptions;

public class ErrorResponse {

  private String message;

  public ErrorResponse(String message) {
    this.message = message;
  }

  // Getters and setters
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
