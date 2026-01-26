package com.testjava.priceservice.infrastructure.persistence.adapter;

import static com.testjava.priceservice.common.TestDataFactory.TEST_PRICE_35_50;
import static com.testjava.priceservice.common.TestDataFactory.TEST_PRICE_38_95;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.testjava.priceservice.domain.model.Price;
import com.testjava.priceservice.infrastructure.persistence.entity.PriceEntity;
import com.testjava.priceservice.infrastructure.persistence.repository.JpaPriceRepository;

class PriceRepositoryAdapterTest {

  @Mock private JpaPriceRepository jpaPriceRepository;

  private PriceRepositoryAdapter priceRepositoryAdapter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    priceRepositoryAdapter =
        new PriceRepositoryAdapter(
            jpaPriceRepository,
            new com.testjava.priceservice.infrastructure.persistence.mapper.PriceEntityMapper());
  }

  @Test
  void shouldMapEntitiesToDomainObjects() {
    // Given
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
    Long productId = 35455L;
    Long brandId = 1L;

    PriceEntity entity =
        new PriceEntity(
            1L,
            LocalDateTime.of(2020, 6, 14, 0, 0),
            LocalDateTime.of(2020, 12, 31, 23, 59),
            1,
            35455L,
            0,
            TEST_PRICE_35_50,
            "EUR");

    when(jpaPriceRepository.findApplicablePrices(applicationDate, productId, brandId))
        .thenReturn(List.of(entity));

    // When
    List<Price> result =
        priceRepositoryAdapter.findApplicablePrices(applicationDate, productId, brandId);

    // Then
    assertEquals(1, result.size());

    Price price = result.get(0);
    assertEquals(1L, price.getBrandId());
    assertEquals(1, price.getPriceList());
    assertEquals(35455L, price.getProductId());
    assertEquals(0, price.getPriority());
    assertEquals(TEST_PRICE_35_50, price.getPrice());
    assertEquals("EUR", price.getCurrency());

    verify(jpaPriceRepository).findApplicablePrices(applicationDate, productId, brandId);
  }

  @Test
  void shouldReturnEmptyListWhenNoEntitiesFound() {
    // Given
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
    Long productId = 35455L;
    Long brandId = 1L;

    when(jpaPriceRepository.findApplicablePrices(applicationDate, productId, brandId))
        .thenReturn(Collections.emptyList());

    // When
    List<Price> result =
        priceRepositoryAdapter.findApplicablePrices(applicationDate, productId, brandId);

    // Then
    assertTrue(result.isEmpty());

    verify(jpaPriceRepository).findApplicablePrices(applicationDate, productId, brandId);
  }

  @Test
  void shouldPreserveMappingAccuracy() {
    // Given
    LocalDateTime startDate = LocalDateTime.of(2020, 6, 15, 16, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 17, 0);

    PriceEntity entity =
        new PriceEntity(1L, startDate, endDate, 4, 35455L, 1, TEST_PRICE_38_95, "EUR");

    when(jpaPriceRepository.findApplicablePrices(applicationDate, 35455L, 1L))
        .thenReturn(List.of(entity));

    // When
    List<Price> result = priceRepositoryAdapter.findApplicablePrices(applicationDate, 35455L, 1L);

    // Then
    assertEquals(1, result.size());
    Price price = result.get(0);

    assertEquals(startDate, price.getStartDate());
    assertEquals(endDate, price.getEndDate());
    assertEquals(4, price.getPriceList());
    assertEquals(TEST_PRICE_38_95, price.getPrice());
  }
}
