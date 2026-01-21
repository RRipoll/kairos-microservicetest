package com.testjava.priceservice.domain.validator;

import com.testjava.priceservice.domain.model.PriceQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static com.testjava.priceservice.common.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Domain Tests - Price Query Validator")
class PriceQueryValidatorTest {

    private PriceQueryValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PriceQueryValidator();
    }

    @Test
    @DisplayName("Should validate correct PriceQuery successfully")
    void shouldValidateCorrectPriceQuerySuccessfully() {
        // Given
        PriceQuery validQuery = new PriceQuery(
            TEST_DATE_2020_06_14_16_00,
            TEST_PRODUCT_ID,
            TEST_BRAND_ID
        );

        // When & Then
        assertThatCode(() -> validator.validate(validQuery))
            .doesNotThrowAnyException();
        
        assertTrue(validator.isValid(validQuery));
    }

    @Test
    @DisplayName("Should throw exception when PriceQuery is null")
    void shouldThrowExceptionWhenPriceQueryIsNull() {
        // When & Then
        assertThatThrownBy(() -> validator.validate(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("PriceQuery cannot be null");
            
        assertFalse(validator.isValid(null));
    }

    @Test
    @DisplayName("Should throw exception when application date is null")
    void shouldThrowExceptionWhenApplicationDateIsNull() {
        // Given
        PriceQuery invalidQuery = new PriceQuery(null, TEST_PRODUCT_ID, TEST_BRAND_ID);

        // When & Then
        assertThatThrownBy(() -> validator.validate(invalidQuery))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Application date is required");
            
        assertFalse(validator.isValid(invalidQuery));
    }

    @Test
    @DisplayName("Should throw exception when product ID is null")
    void shouldThrowExceptionWhenProductIdIsNull() {
        // Given
        PriceQuery invalidQuery = new PriceQuery(TEST_DATE_2020_06_14_16_00, null, TEST_BRAND_ID);

        // When & Then
        assertThatThrownBy(() -> validator.validate(invalidQuery))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product ID is required");
            
        assertFalse(validator.isValid(invalidQuery));
    }

    @Test
    @DisplayName("Should throw exception when brand ID is null")
    void shouldThrowExceptionWhenBrandIdIsNull() {
        // Given
        PriceQuery invalidQuery = new PriceQuery(TEST_DATE_2020_06_14_16_00, TEST_PRODUCT_ID, null);

        // When & Then
        assertThatThrownBy(() -> validator.validate(invalidQuery))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Brand ID is required");
            
        assertFalse(validator.isValid(invalidQuery));
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -999})
    @DisplayName("Should throw exception when product ID is not positive")
    void shouldThrowExceptionWhenProductIdIsNotPositive(Long invalidProductId) {
        // Given
        PriceQuery invalidQuery = new PriceQuery(TEST_DATE_2020_06_14_16_00, invalidProductId, TEST_BRAND_ID);

        // When & Then
        assertThatThrownBy(() -> validator.validate(invalidQuery))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product ID must be positive");
            
        assertFalse(validator.isValid(invalidQuery));
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -999})
    @DisplayName("Should throw exception when brand ID is not positive")
    void shouldThrowExceptionWhenBrandIdIsNotPositive(Long invalidBrandId) {
        // Given
        PriceQuery invalidQuery = new PriceQuery(TEST_DATE_2020_06_14_16_00, TEST_PRODUCT_ID, invalidBrandId);

        // When & Then
        assertThatThrownBy(() -> validator.validate(invalidQuery))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Brand ID must be positive");
            
        assertFalse(validator.isValid(invalidQuery));
    }

    @Test
    @DisplayName("Should validate edge case values successfully")
    void shouldValidateEdgeCaseValuesSuccessfully() {
        // Given
        PriceQuery edgeCaseQuery = new PriceQuery(
            LocalDateTime.of(2020, 1, 1, 0, 0),
            MIN_VALID_ID, // Minimum valid value
            MAX_VALID_ID  // Maximum valid value
        );

        // When & Then
        assertThatCode(() -> validator.validate(edgeCaseQuery))
            .doesNotThrowAnyException();
            
        assertTrue(validator.isValid(edgeCaseQuery));
    }

    @Test
    @DisplayName("Should validate future dates successfully")
    void shouldValidateFutureDatesSuccessfully() {
        // Given
        PriceQuery futureQuery = new PriceQuery(
            LocalDateTime.of(2025, 12, 31, 23, 59),
            TEST_PRODUCT_ID,
            TEST_BRAND_ID
        );

        // When & Then
        assertThatCode(() -> validator.validate(futureQuery))
            .doesNotThrowAnyException();
            
        assertTrue(validator.isValid(futureQuery));
    }

    @Test
    @DisplayName("Should validate past dates successfully")
    void shouldValidatePastDatesSuccessfully() {
        // Given
        PriceQuery pastQuery = new PriceQuery(
            LocalDateTime.of(1990, 1, 1, 0, 0),
            TEST_PRODUCT_ID,
            TEST_BRAND_ID
        );

        // When & Then
        assertThatCode(() -> validator.validate(pastQuery))
            .doesNotThrowAnyException();
            
        assertTrue(validator.isValid(pastQuery));
    }
}