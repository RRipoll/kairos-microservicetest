package com.testjava.priceservice.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Domain Exception Tests - Price Service Exception")
class PriceServiceExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        // Given
        String message = "Service unavailable";

        // When
        PriceServiceException exception = new PriceServiceException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Should create exception with message and cause")
    void shouldCreateExceptionWithMessageAndCause() {
        // Given
        String message = "Database connection failed";
        Throwable cause = new RuntimeException("Connection timeout");

        // When
        PriceServiceException exception = new PriceServiceException(message, cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("Should create exception with cause only")
    void shouldCreateExceptionWithCauseOnly() {
        // Given
        Throwable cause = new IllegalStateException("Invalid state");

        // When
        PriceServiceException exception = new PriceServiceException(cause);

        // Then
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception.getMessage()).isNotNull();
    }

    @Test
    @DisplayName("Should handle null message gracefully")
    void shouldHandleNullMessageGracefully() {
        // When
        PriceServiceException exception = new PriceServiceException((String) null);

        // Then
        assertThat(exception.getMessage()).isNull();
        assertThat(exception.getCause()).isNull();
    }


    @Test
    @DisplayName("Should handle null message and cause gracefully")
    void shouldHandleNullMessageAndCauseGracefully() {
        // When
        PriceServiceException exception = new PriceServiceException(null, null);

        // Then
        assertThat(exception.getMessage()).isNull();
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Should inherit from RuntimeException")
    void shouldInheritFromRuntimeException() {
        // When
        PriceServiceException exception = new PriceServiceException("test");

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should preserve stack trace information")
    void shouldPreserveStackTraceInformation() {
        // When
        PriceServiceException exception = new PriceServiceException("test");

        // Then
        assertThat(exception.getStackTrace()).isNotEmpty();
        assertThat(exception.getStackTrace()[0].getClassName()).contains(getClass().getName());
    }

    @Test
    @DisplayName("Should handle complex nested exceptions")
    void shouldHandleComplexNestedException() {
        // Given
        Exception rootCause = new IllegalArgumentException("Root cause");
        RuntimeException intermediateCause = new RuntimeException("Intermediate", rootCause);
        
        // When
        PriceServiceException exception = new PriceServiceException("Top level", intermediateCause);

        // Then
        assertThat(exception.getMessage()).isEqualTo("Top level");
        assertThat(exception.getCause()).isEqualTo(intermediateCause);
        assertThat(exception.getCause().getCause()).isEqualTo(rootCause);
    }
}