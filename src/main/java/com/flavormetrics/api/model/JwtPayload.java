package com.flavormetrics.api.model;

import java.util.Objects;

public record JwtPayload(String email) {
  public JwtPayload(String email) {
    this.email = Objects.requireNonNull(email, "username cannot be null");
  }
}
