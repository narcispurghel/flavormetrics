package com.flavormetrics.api.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super("User not found");
  }
}
