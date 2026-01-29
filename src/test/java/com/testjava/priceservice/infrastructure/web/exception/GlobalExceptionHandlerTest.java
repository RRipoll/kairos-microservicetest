package com.testjava.priceservice.infrastructure.web.exception;

import static com.testjava.priceservice.common.TestDataFactory.TEST_BRAND_ID;
import static com.testjava.priceservice.common.TestDataFactory.TEST_PRODUCT_ID;
import static com.testjava.priceservice.common.TestDataFactory.VALID_DATE_STRING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.testjava.priceservice.domain.exception.InvalidPriceQueryException;
import com.testjava.priceservice.domain.exception.PriceNotFoundException;
import com.testjava.priceservice.domain.exception.PriceServiceException;

@DisplayName("Exception Handler Tests - Global Exception Handler")
class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler globalExceptionHandler;

  @BeforeEach
  void setUp() {
    globalExceptionHandler = new GlobalExceptionHandler();
  }

  @Test
  @DisplayName("Should handle PriceNotFoundException")
  void shouldHandlePriceNotFoundException() {
    // Given
    PriceNotFoundException exception =
        new PriceNotFoundException(TEST_PRODUCT_ID, TEST_BRAND_ID, VALID_DATE_STRING);

    // When
    ResponseEntity<?> response = globalExceptionHandler.handlePriceNotFoundException(exception);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  @DisplayName("Should handle InvalidPriceQueryException")
  void shouldHandleInvalidPriceQueryException() {
    // Given
    InvalidPriceQueryException exception =
        new InvalidPriceQueryException("productId", 123L, "Invalid query");

    // When
    ResponseEntity<?> response = globalExceptionHandler.handleInvalidPriceQueryException(exception);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  @DisplayName("Should handle PriceServiceException")
  void shouldHandlePriceServiceException() {
    // Given
    PriceServiceException exception = new PriceServiceException("Service error");

    // When
    ResponseEntity<?> response = globalExceptionHandler.handlePriceServiceException(exception);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  @DisplayName("Should handle MissingServletRequestParameterException")
  void shouldHandleMissingServletRequestParameterException() {
    // Given
    MissingServletRequestParameterException exception =
        new MissingServletRequestParameterException("productId", "Long");

    // When
    ResponseEntity<?> response =
        globalExceptionHandler.handleMissingServletRequestParameter(exception);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  @DisplayName("Should handle MethodArgumentTypeMismatchException")
  void shouldHandleMethodArgumentTypeMismatchException() {
    // Given
    MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
    when(exception.getName()).thenReturn("productId");
    when(exception.getValue()).thenReturn("invalid");

    // When
    ResponseEntity<?> response = globalExceptionHandler.handleMethodArgumentTypeMismatch(exception);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  @DisplayName("Should handle generic Exception")
  void shouldHandleGenericException() {
    // Given
    Exception exception = new RuntimeException("Unexpected error");

    // When
    ResponseEntity<?> response = globalExceptionHandler.handleGenericException(exception);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody()).isNotNull();
  }
}
