package com.testjava.priceservice.infrastructure.common;

/** HTTP error messages. */
public final class ErrorMessages {
  public static final String PRICE_NOT_FOUND = "Price Not Found";
  public static final String INVALID_REQUEST = "Invalid Request";
  public static final String VALIDATION_ERROR = "Validation Error";
  public static final String BUSINESS_LOGIC_ERROR = "Business Logic Error";
  public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
  public static final String UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred";

  private ErrorMessages() {
    // Utility class
  }
}
