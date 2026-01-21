package com.testjava.priceservice.domain;

import com.testjava.priceservice.domain.model.Price;
import com.testjava.priceservice.domain.model.PriceQuery;
import com.testjava.priceservice.domain.model.PriceResult;
import com.testjava.priceservice.domain.port.PriceRepository;
import com.testjava.priceservice.domain.service.PriceService;
import com.testjava.priceservice.domain.mapper.PriceDomainMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.testjava.priceservice.common.TestDataFactory.*;

class PriceServiceUnitTest {

        @Mock
        private PriceRepository priceRepository;

        @Mock
        private PriceDomainMapper domainMapper;

        private PriceService priceService;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                priceService = new PriceService(priceRepository, domainMapper);
        }

        @Test
        void shouldReturnHighestPriorityPrice() {
                // Given
                LocalDateTime testDate = LocalDateTime.of(2020, 6, 14, 16, 0);
                Long productId = TEST_PRODUCT_ID;
                Long brandId = TEST_BRAND_ID;

                List<Price> prices = Arrays.asList(
                                new Price(1L, LocalDateTime.of(2020, 6, 14, 15, 0),
                                                LocalDateTime.of(2020, 6, 14, 18, 30),
                                                TEST_PRICE_LIST_2, TEST_PRODUCT_ID, TEST_PRIORITY_1, TEST_PRICE_25_45,
                                                TEST_CURRENCY));

                when(priceRepository.findApplicablePrice(testDate, productId, brandId))
                                .thenReturn(Optional.of(prices.get(0)));

                PriceResult expectedResult = new PriceResult(
                                TEST_PRODUCT_ID, TEST_BRAND_ID, TEST_PRICE_LIST_2,
                                LocalDateTime.of(2020, 6, 14, 15, 0),
                                LocalDateTime.of(2020, 6, 14, 18, 30),
                                TEST_PRICE_25_45);
                when(domainMapper.mapToResult(prices.get(0))).thenReturn(expectedResult);

                PriceQuery query = new PriceQuery(testDate, productId, brandId);

                // When
                Optional<PriceResult> result = priceService.findApplicablePrice(query);

                // Then
                assertTrue(result.isPresent());
                assertEquals(TEST_PRICE_LIST_2, result.get().getPriceList());
                assertEquals(TEST_PRICE_25_45, result.get().getPrice());
                assertEquals(TEST_PRODUCT_ID, result.get().getProductId());
                assertEquals(TEST_BRAND_ID, result.get().getBrandId());
        }

        @Test
        void shouldReturnOnlyApplicablePriceWhenOneExists() {
                // Given
                LocalDateTime testDate = LocalDateTime.of(2020, 6, 14, 10, 0);
                Long productId = TEST_PRODUCT_ID;
                Long brandId = TEST_BRAND_ID;

                List<Price> prices = Arrays.asList(
                                new Price(1L, LocalDateTime.of(2020, 6, 14, 0, 0),
                                                LocalDateTime.of(2020, 12, 31, 23, 59),
                                                TEST_PRICE_LIST_1, TEST_PRODUCT_ID, TEST_PRIORITY_0, TEST_PRICE_35_50,
                                                TEST_CURRENCY));

                when(priceRepository.findApplicablePrice(testDate, productId, brandId))
                                .thenReturn(Optional.of(prices.get(0)));

                PriceResult expectedResult = new PriceResult(
                                35455L, 1L, 1,
                                LocalDateTime.of(2020, 6, 14, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59),
                                TEST_PRICE_35_50);
                when(domainMapper.mapToResult(prices.get(0))).thenReturn(expectedResult);

                PriceQuery query = new PriceQuery(testDate, productId, brandId);

                // When
                Optional<PriceResult> result = priceService.findApplicablePrice(query);

                // Then
                assertTrue(result.isPresent());
                assertEquals(1, result.get().getPriceList());
                assertEquals(TEST_PRICE_35_50, result.get().getPrice());
        }

        @Test
        void shouldReturnEmptyWhenNoPricesFound() {
                // Given
                LocalDateTime testDate = LocalDateTime.of(2020, 6, 14, 16, 0);
                Long productId = TEST_PRODUCT_ID;
                Long brandId = TEST_BRAND_ID;

                when(priceRepository.findApplicablePrice(testDate, productId, brandId))
                                .thenReturn(Optional.empty());

                PriceQuery query = new PriceQuery(testDate, productId, brandId);

                // When
                Optional<PriceResult> result = priceService.findApplicablePrice(query);

                // Then
                assertFalse(result.isPresent());
        }

        @Test
        void shouldReturnEmptyWhenNoPricesAreApplicableAtDate() {
                // Given
                LocalDateTime testDate = LocalDateTime.of(2020, 6, 13, 16, 0);
                Long productId = TEST_PRODUCT_ID;
                Long brandId = TEST_BRAND_ID;

                List<Price> prices = Arrays.asList(
                                new Price(1L, LocalDateTime.of(2020, 6, 14, 0, 0),
                                                LocalDateTime.of(2020, 12, 31, 23, 59),
                                                TEST_PRICE_LIST_1, TEST_PRODUCT_ID, TEST_PRIORITY_0, TEST_PRICE_35_50,
                                                TEST_CURRENCY));

                when(priceRepository.findApplicablePrice(testDate, productId, brandId))
                                .thenReturn(Optional.of(prices.get(0)));

                PriceQuery query = new PriceQuery(testDate, productId, brandId);

                // When
                Optional<PriceResult> result = priceService.findApplicablePrice(query);

                // Then
                assertFalse(result.isPresent());
        }

        @Test
        void shouldSelectHighestPriorityAmongMultipleApplicablePrices() {
                // Given
                LocalDateTime testDate = LocalDateTime.of(2020, 6, 15, 17, 0);
                Long productId = TEST_PRODUCT_ID;
                Long brandId = TEST_BRAND_ID;

                List<Price> prices = Arrays.asList(
                                new Price(1L, LocalDateTime.of(2020, 6, 15, 16, 0),
                                                LocalDateTime.of(2020, 12, 31, 23, 59),
                                                4, 35455L, 1, TEST_PRICE_38_95, "EUR"));

                when(priceRepository.findApplicablePrice(testDate, productId, brandId))
                                .thenReturn(Optional.of(prices.get(0)));

                PriceResult expectedResult = new PriceResult(
                                35455L, 1L, 4,
                                LocalDateTime.of(2020, 6, 15, 16, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59),
                                TEST_PRICE_38_95);
                when(domainMapper.mapToResult(prices.get(0))).thenReturn(expectedResult);

                PriceQuery query = new PriceQuery(testDate, productId, brandId);

                // When
                Optional<PriceResult> result = priceService.findApplicablePrice(query);

                // Then
                assertTrue(result.isPresent());
                assertEquals(4, result.get().getPriceList());
                assertEquals(TEST_PRICE_38_95, result.get().getPrice());
        }
}