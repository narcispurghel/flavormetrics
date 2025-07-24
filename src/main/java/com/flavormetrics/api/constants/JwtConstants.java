package com.flavormetrics.api.constants;

public final class JwtConstants {

  public static final String ISSUER = "flavormetrics";
  public static final String AUDIENCE = "flavormetrics";
  public static final int ACCESS_TOKEN_EXPIRATION = 300000; // 300000 -> 5m
  public static final int REFRESH_TOKEN_EXPIRATION = 3600000; // 3600000ms -> 1h
  public static final String ACCESS_TOKEN_NAME = "accessToken";
  public static final String REFRESH_TOKEN_NAME = "refreshToken";
  public static final int ACCESS_TOKEN_THRESHOLD = 30000; // 30000ms -> 30s

  private JwtConstants() {}
}
