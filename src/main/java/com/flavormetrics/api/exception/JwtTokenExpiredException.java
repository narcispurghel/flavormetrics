package com.flavormetrics.api.exception;

public class JwtTokenExpiredException extends JwtException {

  public JwtTokenExpiredException(String msg) {
    super(msg);
  }
}
