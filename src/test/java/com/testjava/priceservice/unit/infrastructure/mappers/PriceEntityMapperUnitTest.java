package com.testjava.priceservice.unit.infrastructure.mappers;

import com.testjava.priceservice.common.TestCategories;
import com.testjava.priceservice.common.TestDataFactory;
import com.testjava.priceservice.domain.model.Price;
import com.testjava.priceservice.infrastructure.persistence.entity.PriceEntity;
import com.testjava.priceservice.infrastructure.persistence.mapper.PriceEntityMapper;
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
@DisplayName("Unit Tests - Price Entity Mapper")
class PriceEntityMapperUnitTest implements TestCategories.UnitTest {

    private PriceEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PriceEntityMapper();
    }

    @Test
    @DisplayName("Should map PriceEntity to Price domain object correctly")
    void shouldMapPriceEntityToPriceDomainObjectCorrectly() {
        // Given
        PriceEntity entity = TestDataFactory.createTestPriceEntity();

        // When
        Price price = mapper.mapToDomain(entity);

        // Then
        assertThat(price).isNotNull();
        assertThat(price.getBrandId()).isEqualTo(entity.getBrandId());
        assertThat(price.getStartDate()).isEqualTo(entity.getStartDate());
        assertThat(price.getEndDate()).isEqualTo(entity.getEndDate());
        assertThat(price.getPriceList()).isEqualTo(entity.getPriceList());
        assertThat(price.getProductId()).isEqualTo(entity.getProductId());
        assertThat(price.getPriority()).isEqualTo(entity.getPriority());
        assertThat(price.getPrice()).isEqualTo(entity.getPrice());
        assertThat(price.getCurrency()).isEqualTo(entity.getCurrency());
    }

    @Test
    @DisplayName("Should handle null input gracefully")
    void shouldHandleNullInputGracefully() {
        // When & Then
        assertThatThrownBy(() -> mapper.mapToDomain(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("PriceEntity cannot be null");
    }

    @Test
    @DisplayName("Should map entity with custom values correctly")
    void shouldMapEntityWithCustomValuesCorrectly() {
        // Given
        LocalDateTime customStart = LocalDateTime.of(2020, 6, 15, 16, 0);
        LocalDateTime customEnd = LocalDateTime.of(2020, 12, 31, 23, 59);
        PriceEntity entity = new PriceEntity(
            2L,     // brandId
            customStart,
            customEnd,
            4,      // priceList
            99999L, // productId
            1,      // priority
            TEST_PRICE_38_95,
            "USD"   // currency
        );

        // When
        Price price = mapper.mapToDomain(entity);

        // Then
        assertThat(price.getBrandId()).isEqualTo(2L);
        assertThat(price.getStartDate()).isEqualTo(customStart);
        assertThat(price.getEndDate()).isEqualTo(customEnd);
        assertThat(price.getPriceList()).isEqualTo(4);
        assertThat(price.getProductId()).isEqualTo(99999L);
        assertThat(price.getPriority()).isEqualTo(1);
        assertThat(price.getPrice()).isEqualTo(TEST_PRICE_38_95);
        assertThat(price.getCurrency()).isEqualTo("USD");
    }

    @Test
    @DisplayName("Should preserve precision for BigDecimal values")
    void shouldPreservePrecisionForBigDecimalValues() {
        // Given
        BigDecimal precisePrice = TEST_PRICE_PRECISE;
        PriceEntity entity = new PriceEntity(
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
        Price price = mapper.mapToDomain(entity);

        // Then
        assertThat(price.getPrice()).isEqualTo(precisePrice);
        assertThat(price.getPrice().scale()).isEqualTo(precisePrice.scale());
    }

    @Test
    @DisplayName("Should handle edge case values correctly")
    void shouldHandleEdgeCaseValuesCorrectly() {
        // Given
        PriceEntity entity = new PriceEntity(
            Long.MAX_VALUE,
            LocalDateTime.MIN,
            LocalDateTime.MAX,
            Integer.MAX_VALUE,
            1L,
            Integer.MAX_VALUE,
            BigDecimal.ZERO,
            "EUR"
        );

        // When
        Price price = mapper.mapToDomain(entity);

        // Then
        assertThat(price.getBrandId()).isEqualTo(Long.MAX_VALUE);
        assertThat(price.getStartDate()).isEqualTo(LocalDateTime.MIN);
        assertThat(price.getEndDate()).isEqualTo(LocalDateTime.MAX);
        assertThat(price.getPriceList()).isEqualTo(Integer.MAX_VALUE);
        assertThat(price.getProductId()).isEqualTo(1L);
        assertThat(price.getPriority()).isEqualTo(Integer.MAX_VALUE);
        assertThat(price.getPrice()).isEqualTo(BigDecimal.ZERO);
        assertThat(price.getCurrency()).isEqualTo("EUR");
    }

    @Test
    @DisplayName("Should map multiple entities consistently")
    void shouldMapMultipleEntitiesConsistently() {
        // Given
        PriceEntity entity1 = TestDataFactory.createTestPriceEntity(1, 0, TEST_PRICE_35_50);
        PriceEntity entity2 = TestDataFactory.createTestPriceEntity(2, 1, TEST_PRICE_25_45);

        // When
        Price price1 = mapper.mapToDomain(entity1);
        Price price2 = mapper.mapToDomain(entity2);

        // Then
        assertThat(price1.getPriceList()).isEqualTo(1);
        assertThat(price1.getPriority()).isEqualTo(0);
        assertThat(price1.getPrice()).isEqualTo(TEST_PRICE_35_50);

        assertThat(price2.getPriceList()).isEqualTo(2);
        assertThat(price2.getPriority()).isEqualTo(1);
        assertThat(price2.getPrice()).isEqualTo(TEST_PRICE_25_45);
    }

    @Test
    @DisplayName("Should validate date consistency after mapping")
    void shouldValidateDateConsistencyAfterMapping() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 23, 59);
        PriceEntity entity = new PriceEntity(
            TestDataFactory.TEST_BRAND_ID,
            startDate,
            endDate,
            1,
            TestDataFactory.TEST_PRODUCT_ID,
            0,
            TEST_PRICE_35_50,
            TestDataFactory.TEST_CURRENCY
        );

        // When
        Price price = mapper.mapToDomain(entity);

        // Then
        assertThat(price.getStartDate()).isEqualTo(startDate);
        assertThat(price.getEndDate()).isEqualTo(endDate);
        assertThat(price.getStartDate()).isBefore(price.getEndDate());
    }

    @Test
    @DisplayName("Should map Price to PriceEntity correctly")
    void shouldMapPriceToPriceEntityCorrectly() {
        // Given
        Price price = TestDataFactory.createTestPrice();

        // When
        PriceEntity entity = mapper.mapToEntity(price);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getBrandId()).isEqualTo(price.getBrandId());
        assertThat(entity.getStartDate()).isEqualTo(price.getStartDate());
        assertThat(entity.getEndDate()).isEqualTo(price.getEndDate());
        assertThat(entity.getPriceList()).isEqualTo(price.getPriceList());
        assertThat(entity.getProductId()).isEqualTo(price.getProductId());
        assertThat(entity.getPriority()).isEqualTo(price.getPriority());
        assertThat(entity.getPrice()).isEqualTo(price.getPrice());
        assertThat(entity.getCurrency()).isEqualTo(price.getCurrency());
    }

    @Test
    @DisplayName("Should handle null price in mapToEntity")
    void shouldHandleNullPriceInMapToEntity() {
        // When & Then
        assertThatThrownBy(() -> mapper.mapToEntity(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Price cannot be null");
    }
}