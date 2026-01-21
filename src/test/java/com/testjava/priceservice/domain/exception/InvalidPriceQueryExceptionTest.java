package com.testjava.priceservice.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.testjava.priceservice.common.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Domain Exception Tests - Invalid Price Query Exception")
class InvalidPriceQueryExceptionTest {

    @Test
    @DisplayName("Should create exception with all parameters")
    void shouldCreateExceptionWithAllParameters() {
        // Given
        String field = "productId";
        String reason = PRODUCT_ID_POSITIVE_ERROR;

        // When
        InvalidPriceQueryException exception = new InvalidPriceQueryException(
            field, TEST_PRODUCT_ID, reason
        );

        // Then
        assertThat(exception.getField()).isEqualTo(field);
        assertThat(exception.getValue()).isEqualTo(TEST_PRODUCT_ID);
        assertThat(exception.getMessage()).contains(reason);
        assertThat(exception.getMessage()).contains(field);
        assertThat(exception.getMessage()).contains(TEST_PRODUCT_ID.toString());
    }

    @Test
    @DisplayName("Should create exception with cause")
    void shouldCreateExceptionWithCause() {
        // Given
        String field = "brandId";
        String reason = BRAND_ID_NULL_ERROR;
        Throwable cause = new IllegalArgumentException("Validation error");

        // When
        InvalidPriceQueryException exception = new InvalidPriceQueryException(
            field, TEST_BRAND_ID, reason, cause
        );

        // Then
        assertThat(exception.getField()).isEqualTo(field);
        assertThat(exception.getValue()).isEqualTo(TEST_BRAND_ID);
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception.getMessage()).contains(reason);
    }

    @Test
    @DisplayName("Should handle null field gracefully")
    void shouldHandleNullFieldGracefully() {
        // When
        InvalidPriceQueryException exception = new InvalidPriceQueryException(
            null, TEST_PRODUCT_ID, "Some reason"
        );

        // Then
        assertThat(exception.getField()).isNull();
        assertThat(exception.getValue()).isEqualTo(TEST_PRODUCT_ID);
        assertThat(exception.getMessage()).isNotNull();
    }

    @Test
    @DisplayName("Should handle null value gracefully")
    void shouldHandleNullValueGracefully() {
        // When
        InvalidPriceQueryException exception = new InvalidPriceQueryException(
            "testField", null, "Some reason"
        );

        // Then
        assertThat(exception.getField()).isEqualTo("testField");
        assertThat(exception.getValue()).isNull();
        assertThat(exception.getMessage()).isNotNull();
    }

    @Test
    @DisplayName("Should handle null reason gracefully")
    void shouldHandleNullReasonGracefully() {
        // When
        InvalidPriceQueryException exception = new InvalidPriceQueryException(
            "testField", TEST_PRODUCT_ID, null
        );

        // Then
        assertThat(exception.getField()).isEqualTo("testField");
        assertThat(exception.getValue()).isEqualTo(TEST_PRODUCT_ID);
        assertThat(exception.getMessage()).isNotNull();
    }

    @Test
    @DisplayName("Should inherit from PriceServiceException")
    void shouldInheritFromPriceServiceException() {
        // When
        InvalidPriceQueryException exception = new InvalidPriceQueryException(
            "field", "value", "reason"
        );

        // Then
        assertThat(exception).isInstanceOf(PriceServiceException.class);
    }

    @Test
    @DisplayName("Should handle different value types")
    void shouldHandleDifferentValueTypes() {
        // When
        InvalidPriceQueryException stringException = new InvalidPriceQueryException(
            "stringField", "stringValue", "reason"
        );
        InvalidPriceQueryException numberException = new InvalidPriceQueryException(
            "numberField", 123, "reason"
        );

        // Then
        assertThat(stringException.getValue()).isEqualTo("stringValue");
        assertThat(numberException.getValue()).isEqualTo(123);
    }
}