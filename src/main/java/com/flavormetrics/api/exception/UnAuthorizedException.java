package com.flavormetrics.api.exception;

public class UnAuthorizedException extends RuntimeException {

  public UnAuthorizedException(String msg) {
    super(msg);
  }

  public UnAuthorizedException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
