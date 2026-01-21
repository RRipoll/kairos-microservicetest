package com.testjava.priceservice.unit.infrastructure.mappers;

import com.testjava.priceservice.common.TestCategories;
import com.testjava.priceservice.common.TestDataFactory;
import com.testjava.priceservice.domain.mapper.PriceDomainMapper;
import com.testjava.priceservice.domain.model.Price;
import com.testjava.priceservice.domain.model.PriceResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static com.testjava.priceservice.common.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag(UNIT_TAG)
@DisplayName("Unit Tests - Price Domain Mapper")
class PriceDomainMapperUnitTest implements TestCategories.UnitTest {

    private PriceDomainMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PriceDomainMapper();
    }

    @Test
    @DisplayName("Should map Price to PriceResult correctly")
    void shouldMapPriceToPriceResultCorrectly() {
        // Given
        Price price = TestDataFactory.createTestPrice();

        // When
        PriceResult result = mapper.mapToResult(price);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(price.getProductId());
        assertThat(result.getBrandId()).isEqualTo(price.getBrandId());
        assertThat(result.getPriceList()).isEqualTo(price.getPriceList());
        assertThat(result.getStartDate()).isEqualTo(price.getStartDate());
        assertThat(result.getEndDate()).isEqualTo(price.getEndDate());
        assertThat(result.getPrice()).isEqualTo(price.getPrice());
    }

    @Test
    @DisplayName("Should handle null input gracefully")
    void shouldHandleNullInputGracefully() {
        // When & Then
        assertThatThrownBy(() -> mapper.mapToResult(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Price cannot be null");
    }

    @Test
    @DisplayName("Should map all field values correctly")
    void shouldMapAllFieldValuesCorrectly() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 15, 16, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);
        Price price = new Price(
            2L,     // brandId
            startDate,
            endDate,
            4,      // priceList
            99999L, // productId
            1,      // priority
            TEST_PRICE_38_95,
            "USD"   // currency
        );

        // When
        PriceResult result = mapper.mapToResult(price);

        // Then
        assertThat(result.getProductId()).isEqualTo(99999L);
        assertThat(result.getBrandId()).isEqualTo(2L);
        assertThat(result.getPriceList()).isEqualTo(4);
        assertThat(result.getStartDate()).isEqualTo(startDate);
        assertThat(result.getEndDate()).isEqualTo(endDate);
        assertThat(result.getPrice()).isEqualTo(TEST_PRICE_38_95);
    }

    @Test
    @DisplayName("Should handle edge case values during mapping")
    void shouldHandleEdgeCaseValuesDuringMapping() {
        // Given
        Price price = new Price(
            Long.MAX_VALUE, // brandId
            LocalDateTime.MIN,
            LocalDateTime.MAX,
            Integer.MAX_VALUE, // priceList
            1L,             // productId
            Integer.MAX_VALUE, // priority
            BigDecimal.ZERO,
            "EUR"
        );

        // When
        PriceResult result = mapper.mapToResult(price);

        // Then
        assertThat(result.getBrandId()).isEqualTo(Long.MAX_VALUE);
        assertThat(result.getStartDate()).isEqualTo(LocalDateTime.MIN);
        assertThat(result.getEndDate()).isEqualTo(LocalDateTime.MAX);
        assertThat(result.getPriceList()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getPrice()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should maintain precision for BigDecimal values")
    void shouldMaintainPrecisionForBigDecimalValues() {
        // Given
        BigDecimal precisePrice = new BigDecimal("999.123456789");
        Price price = new Price(
            TestDataFactory.TEST_BRAND_ID,
            TestDataFactory.TEST_START_DATE,
            TestDataFactory.TEST_END_DATE,
            1,
            TestDataFactory.TEST_PRODUCT_ID,
            0,
            precisePrice,
            TestDataFactory.TEST_CURRENCY
        );

        // When
        PriceResult result = mapper.mapToResult(price);

        // Then
        assertThat(result.getPrice()).isEqualTo(precisePrice);
        assertThat(result.getPrice().scale()).isEqualTo(precisePrice.scale());
    }

    @Test
    @DisplayName("Should map multiple prices consistently")
    void shouldMapMultiplePricesConsistently() {
        // Given
        Price price1 = TestDataFactory.createTestPrice(1, 0, TEST_PRICE_35_50);
        Price price2 = TestDataFactory.createTestPrice(2, 1, TEST_PRICE_25_45);

        // When
        PriceResult result1 = mapper.mapToResult(price1);
        PriceResult result2 = mapper.mapToResult(price2);

        // Then
        assertThat(result1.getPriceList()).isEqualTo(1);
        assertThat(result1.getPrice()).isEqualTo(TEST_PRICE_35_50);
        
        assertThat(result2.getPriceList()).isEqualTo(2);
        assertThat(result2.getPrice()).isEqualTo(TEST_PRICE_25_45);
    }
}