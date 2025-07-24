package com.flavormetrics.api.model;

public record Data<T>(T data) {
  public static <B> Data<B> body(B body) {
    return new Data<>(body);
  }
}
