package com.testjava.priceservice.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.testjava.priceservice.common.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Domain Exception Tests - Price Not Found Exception")
class PriceNotFoundExceptionTest {

    @Test
    @DisplayName("Should handle null parameters gracefully")
    void shouldHandleNullParametersGracefully() {
        // When
        PriceNotFoundException exception = new PriceNotFoundException(null, null, null);

        // Then
        assertThat(exception.getProductId()).isNull();
        assertThat(exception.getBrandId()).isNull();
        assertThat(exception.getApplicationDate()).isNull();
        assertThat(exception.getMessage()).isNotNull();
    }

    @Test
    @DisplayName("Should inherit from PriceServiceException")
    void shouldInheritFromPriceServiceException() {
        // When
        PriceNotFoundException exception = new PriceNotFoundException(
                TEST_PRODUCT_ID, TEST_BRAND_ID, VALID_DATE_STRING);

        // Then
        assertThat(exception).isInstanceOf(PriceServiceException.class);
    }

    @Test
    @DisplayName("Should create exception with all parameters")
    void shouldCreateExceptionWithAllParameters() {
        // When
        PriceNotFoundException exception = new PriceNotFoundException(
                TEST_PRODUCT_ID, TEST_BRAND_ID, VALID_DATE_STRING);

        // Then
        assertThat(exception.getProductId()).isEqualTo(TEST_PRODUCT_ID);
        assertThat(exception.getBrandId()).isEqualTo(TEST_BRAND_ID);
        assertThat(exception.getApplicationDate()).isEqualTo(VALID_DATE_STRING);
        assertThat(exception.getMessage()).contains("No applicable price found");
    }

    @Test
    @DisplayName("Should create exception with cause")
    void shouldCreateExceptionWithCause() {
        // Given
        Throwable cause = new RuntimeException("Database error");

        // When
        PriceNotFoundException exception = new PriceNotFoundException(
                TEST_PRODUCT_ID, TEST_BRAND_ID, VALID_DATE_STRING, cause);

        // Then
        assertThat(exception.getProductId()).isEqualTo(TEST_PRODUCT_ID);
        assertThat(exception.getBrandId()).isEqualTo(TEST_BRAND_ID);
        assertThat(exception.getApplicationDate()).isEqualTo(VALID_DATE_STRING);
        assertThat(exception.getCause()).isEqualTo(cause);
    }
}