package com.flavormetrics.api.exception;

public class MaximumNumberOfRatingException extends RuntimeException {

  public MaximumNumberOfRatingException() {
    super("Maximum number of ratings exceeded");
  }
}
