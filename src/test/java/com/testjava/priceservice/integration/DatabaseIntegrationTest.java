package com.testjava.priceservice.integration;

import com.testjava.priceservice.common.TestCategories;
import com.testjava.priceservice.common.TestDataFactory;
import com.testjava.priceservice.infrastructure.persistence.entity.PriceEntity;
import com.testjava.priceservice.infrastructure.persistence.repository.JpaPriceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static com.testjava.priceservice.common.TestDataFactory.*;

@DataJpaTest
@ActiveProfiles(TEST_PROFILE)
@Tag(INTEGRATION_TAG)
@DisplayName("Integration Tests - Database Layer")
class DatabaseIntegrationTest implements TestCategories.IntegrationTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private JpaPriceRepository priceRepository;

        @Test
        @DisplayName("Should query database and return highest priority price")
        void shouldQueryDatabaseAndReturnHighestPriorityPrice() {
                // Given
                LocalDateTime applicationDate = TestDataFactory.TEST_DATE_2020_06_14_16_00;

                // Create test data with different priorities
                PriceEntity lowPriorityPrice = new PriceEntity(
                                1L, LocalDateTime.of(2020, 6, 14, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59),
                                1, TEST_PRODUCT_ID, 0, TEST_PRICE_35_50, TEST_CURRENCY);

                PriceEntity highPriorityPrice = new PriceEntity(
                                1L, LocalDateTime.of(2020, 6, 14, 15, 0),
                                LocalDateTime.of(2020, 6, 14, 18, 30),
                                2, TEST_PRODUCT_ID, 1, TEST_PRICE_25_45, TEST_CURRENCY);

                entityManager.persistAndFlush(lowPriorityPrice);
                entityManager.persistAndFlush(highPriorityPrice);

                // When
                Optional<PriceEntity> result = priceRepository.findApplicablePrice(
                                applicationDate, TEST_PRODUCT_ID, 1L);

                // Then
                assertThat(result).isPresent();
                PriceEntity price = result.get();
                assertThat(price.getPriority()).isEqualTo(1); // Highest priority
                assertThat(price.getPrice()).isEqualTo(TEST_PRICE_25_45);
                assertThat(price.getPriceList()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should return empty list when no prices match criteria")
        void shouldReturnEmptyListWhenNoPricesMatchCriteria() {
                // Given
                LocalDateTime futureDate = LocalDateTime.of(2025, 1, 1, 10, 0);

                PriceEntity pastPrice = new PriceEntity(
                                1L, LocalDateTime.of(2020, 6, 14, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59),
                                1, TEST_PRODUCT_ID, 0, TEST_PRICE_35_50, TEST_CURRENCY);

                entityManager.persistAndFlush(pastPrice);

                // When
                Optional<PriceEntity> result = priceRepository.findApplicablePrice(
                                futureDate, TEST_PRODUCT_ID, 1L);

                // Then
                assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle date range boundaries correctly")
        void shouldHandleDateRangeBoundariesCorrectly() {
                // Given
                LocalDateTime exactStartTime = LocalDateTime.of(2020, 6, 14, 15, 0, 0);
                LocalDateTime exactEndTime = LocalDateTime.of(2020, 6, 14, 18, 30, 0);

                PriceEntity exactBoundaryPrice = new PriceEntity(
                                1L, exactStartTime, exactEndTime,
                                2, TEST_PRODUCT_ID, 1, TEST_PRICE_25_45, TEST_CURRENCY);

                entityManager.persistAndFlush(exactBoundaryPrice);

                // When - Test exact start boundary
                Optional<PriceEntity> startResult = priceRepository.findApplicablePrice(
                                exactStartTime, TEST_PRODUCT_ID, 1L);

                // When - Test exact end boundary
                Optional<PriceEntity> endResult = priceRepository.findApplicablePrice(
                                exactEndTime, TEST_PRODUCT_ID, 1L);

                // Then
                assertThat(startResult).isPresent();
                assertThat(endResult).isPresent();
                assertThat(startResult.get().getId()).isEqualTo(endResult.get().getId());
        }

        @Test
        @DisplayName("Should handle multiple overlapping prices with different priorities")
        void shouldHandleMultipleOverlappingPricesWithDifferentPriorities() {
                // Given
                LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 10, 0);

                // Create overlapping price periods with different priorities
                PriceEntity basePrice = new PriceEntity(
                                1L, LocalDateTime.of(2020, 6, 14, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59),
                                1, TEST_PRODUCT_ID, 0, TEST_PRICE_35_50, TEST_CURRENCY);

                PriceEntity morningPrice = new PriceEntity(
                                1L, LocalDateTime.of(2020, 6, 15, 0, 0),
                                LocalDateTime.of(2020, 6, 15, 11, 0),
                                3, TEST_PRODUCT_ID, 1, TEST_PRICE_30_50, TEST_CURRENCY);

                entityManager.persistAndFlush(basePrice);
                entityManager.persistAndFlush(morningPrice);

                // When
                Optional<PriceEntity> result = priceRepository.findApplicablePrice(
                                applicationDate, TEST_PRODUCT_ID, 1L);

                // Then
                assertThat(result).isPresent();
                PriceEntity price = result.get();
                assertThat(price.getPriority()).isEqualTo(1); // Higher priority
                assertThat(price.getPrice()).isEqualTo(TEST_PRICE_30_50);
                assertThat(price.getPriceList()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should filter by product ID correctly")
        void shouldFilterByProductIdCorrectly() {
                // Given
                LocalDateTime applicationDate = TestDataFactory.TEST_DATE_2020_06_14_16_00;

                PriceEntity correctProduct = new PriceEntity(
                                1L, TestDataFactory.TEST_START_DATE, TestDataFactory.TEST_END_DATE,
                                1, TEST_PRODUCT_ID, 0, TEST_PRICE_35_50, TEST_CURRENCY);

                PriceEntity differentProduct = new PriceEntity(
                                1L, TestDataFactory.TEST_START_DATE, TestDataFactory.TEST_END_DATE,
                                1, TEST_PRODUCT_ID_OTHER, 0, TEST_PRICE_45_50, TEST_CURRENCY);

                entityManager.persistAndFlush(correctProduct);
                entityManager.persistAndFlush(differentProduct);

                // When
                Optional<PriceEntity> result = priceRepository.findApplicablePrice(
                                applicationDate, TEST_PRODUCT_ID, 1L);

                // Then
                assertThat(result).isPresent();
                assertThat(result.get().getProductId()).isEqualTo(TEST_PRODUCT_ID);
                assertThat(result.get().getPrice()).isEqualTo(TEST_PRICE_35_50);
        }

        @Test
        @DisplayName("Should filter by brand ID correctly")
        void shouldFilterByBrandIdCorrectly() {
                // Given
                LocalDateTime applicationDate = TestDataFactory.TEST_DATE_2020_06_14_16_00;

                PriceEntity correctBrand = new PriceEntity(
                                1L, TestDataFactory.TEST_START_DATE, TestDataFactory.TEST_END_DATE,
                                1, TEST_PRODUCT_ID, 0, TEST_PRICE_35_50, TEST_CURRENCY);

                PriceEntity differentBrand = new PriceEntity(
                                2L, TestDataFactory.TEST_START_DATE, TestDataFactory.TEST_END_DATE,
                                1, TEST_PRODUCT_ID, 0, TEST_PRICE_40_50, TEST_CURRENCY);

                entityManager.persistAndFlush(correctBrand);
                entityManager.persistAndFlush(differentBrand);

                // When
                Optional<PriceEntity> result = priceRepository.findApplicablePrice(
                                applicationDate, TEST_PRODUCT_ID, 1L);

                // Then
                assertThat(result).isPresent();
                assertThat(result.get().getBrandId()).isEqualTo(1L);
                assertThat(result.get().getPrice()).isEqualTo(TEST_PRICE_35_50);
        }

        @Test
        @DisplayName("Should use database indexes efficiently for query performance")
        void shouldUseDatabaseIndexesEfficientlyForQueryPerformance() {
                // Given
                LocalDateTime applicationDate = TestDataFactory.TEST_DATE_2020_06_14_16_00;

                // Create multiple price entities to test index usage
                for (int i = 0; i < 100; i++) {
                        PriceEntity entity = new PriceEntity(
                                        (long) (i % 5 + 1), // Different brand IDs
                                        LocalDateTime.of(2020, 6, 14, 0, 0),
                                        LocalDateTime.of(2020, 12, 31, 23, 59),
                                        i % 4 + 1, // Different price lists
                                        (long) (35455 + i % 10), // Different product IDs
                                        i % 3, // Different priorities
                                        new BigDecimal(String.valueOf(35.50 + i)),
                                        TEST_CURRENCY);
                        entityManager.persist(entity);
                }
                entityManager.flush();

                // When - Query should use indexes efficiently
                long startTime = System.currentTimeMillis();
                Optional<PriceEntity> result = priceRepository.findApplicablePrice(
                                applicationDate, TEST_PRODUCT_ID, 1L);
                long endTime = System.currentTimeMillis();

                // Then
                assertThat(result).isPresent();
                assertThat(endTime - startTime).isLessThan(1000); // Should be fast due to indexes
        }
}
