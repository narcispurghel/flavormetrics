package com.flavormetrics.api.exception;

public class InvalidImageException extends RuntimeException {

  public InvalidImageException() {
    super("Invalid file provided. File cannot be null or empty.");
  }

  public InvalidImageException(String msg) {
    super(msg);
  }

  public InvalidImageException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
