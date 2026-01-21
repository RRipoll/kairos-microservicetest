package com.testjava.priceservice.unit.infrastructure.mappers;

import com.testjava.priceservice.common.TestCategories;
import com.testjava.priceservice.common.TestDataFactory;
import com.testjava.priceservice.domain.model.PriceResult;
import com.testjava.priceservice.infrastructure.web.dto.PriceResponse;
import com.testjava.priceservice.infrastructure.web.mapper.PriceResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static com.testjava.priceservice.common.TestDataFactory.*;

@Tag(UNIT_TAG)
@DisplayName("Unit Tests - Price Response Mapper")
class PriceResponseMapperUnitTest implements TestCategories.UnitTest {

    private PriceResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PriceResponseMapper();
    }

    @Test
    @DisplayName("Should map PriceResult to PriceResponse correctly")
    void shouldMapPriceResultToPriceResponseCorrectly() {
        // Given
        LocalDateTime startDate = TestDataFactory.TEST_START_DATE;
        LocalDateTime endDate = TestDataFactory.TEST_END_DATE;
        PriceResult priceResult = new PriceResult(
            TestDataFactory.TEST_PRODUCT_ID,
            TestDataFactory.TEST_BRAND_ID,
            1,
            startDate,
            endDate,
            TEST_PRICE_35_50
        );

        // When
        PriceResponse response = mapper.mapToResponse(priceResult);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isEqualTo(TestDataFactory.TEST_PRODUCT_ID);
        assertThat(response.getBrandId()).isEqualTo(TestDataFactory.TEST_BRAND_ID);
        assertThat(response.getPriceList()).isEqualTo(1);
        assertThat(response.getStartDate()).isEqualTo(startDate);
        assertThat(response.getEndDate()).isEqualTo(endDate);
        assertThat(response.getPrice()).isEqualTo(TEST_PRICE_35_50);
    }

    @Test
    @DisplayName("Should handle null input gracefully")
    void shouldHandleNullInputGracefully() {
        // When & Then
        assertThatThrownBy(() -> mapper.mapToResponse(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("PriceResult cannot be null");
    }

    @Test
    @DisplayName("Should preserve all field values during mapping")
    void shouldPreserveAllFieldValuesDuringMapping() {
        // Given
        LocalDateTime customStartDate = LocalDateTime.of(2020, 6, 15, 16, 0);
        LocalDateTime customEndDate = LocalDateTime.of(2020, 12, 31, 23, 59);
        PriceResult priceResult = new PriceResult(
            99999L, // Custom product ID
            2L,     // Custom brand ID
            4,      // Custom price list
            customStartDate,
            customEndDate,
            TEST_PRICE_38_95
        );

        // When
        PriceResponse response = mapper.mapToResponse(priceResult);

        // Then
        assertThat(response.getProductId()).isEqualTo(99999L);
        assertThat(response.getBrandId()).isEqualTo(2L);
        assertThat(response.getPriceList()).isEqualTo(4);
        assertThat(response.getStartDate()).isEqualTo(customStartDate);
        assertThat(response.getEndDate()).isEqualTo(customEndDate);
        assertThat(response.getPrice()).isEqualTo(TEST_PRICE_38_95);
    }

    @Test
    @DisplayName("Should handle edge case values correctly")
    void shouldHandleEdgeCaseValuesCorrectly() {
        // Given
        PriceResult priceResult = new PriceResult(
            1L,     // Minimum ID
            Long.MAX_VALUE, // Maximum ID
            0,      // Minimum price list
            LocalDateTime.MIN,
            LocalDateTime.MAX,
            BigDecimal.ZERO
        );

        // When
        PriceResponse response = mapper.mapToResponse(priceResult);

        // Then
        assertThat(response.getProductId()).isEqualTo(1L);
        assertThat(response.getBrandId()).isEqualTo(Long.MAX_VALUE);
        assertThat(response.getPriceList()).isEqualTo(0);
        assertThat(response.getStartDate()).isEqualTo(LocalDateTime.MIN);
        assertThat(response.getEndDate()).isEqualTo(LocalDateTime.MAX);
        assertThat(response.getPrice()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should maintain precision for decimal values")
    void shouldMaintainPrecisionForDecimalValues() {
        // Given
        BigDecimal precisePrice = TEST_PRICE_PRECISE;
        PriceResult priceResult = new PriceResult(
            TestDataFactory.TEST_PRODUCT_ID,
            TestDataFactory.TEST_BRAND_ID,
            1,
            TestDataFactory.TEST_START_DATE,
            TestDataFactory.TEST_END_DATE,
            precisePrice
        );

        // When
        PriceResponse response = mapper.mapToResponse(priceResult);

        // Then
        assertThat(response.getPrice()).isEqualTo(precisePrice);
        assertThat(response.getPrice().scale()).isEqualTo(precisePrice.scale());
    }
}