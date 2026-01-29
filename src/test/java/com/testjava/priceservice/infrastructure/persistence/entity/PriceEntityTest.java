package com.testjava.priceservice.infrastructure.persistence.entity;

import static com.testjava.priceservice.common.TestDataFactory.TEST_PRICE_25_45;
import static com.testjava.priceservice.common.TestDataFactory.TEST_PRICE_35_50;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class PriceEntityTest {

  @Test
  void shouldCreatePriceEntityWithAllFields() {
    // Given
    LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);

    // When
    PriceEntity entity =
        new PriceEntity(1L, startDate, endDate, 1, 35455L, 0, TEST_PRICE_35_50, "EUR");

    // Then
    assertEquals(1L, entity.getBrandId());
    assertEquals(startDate, entity.getStartDate());
    assertEquals(endDate, entity.getEndDate());
    assertEquals(1, entity.getPriceList());
    assertEquals(35455L, entity.getProductId());
    assertEquals(0, entity.getPriority());
    assertEquals(TEST_PRICE_35_50, entity.getPrice());
    assertEquals("EUR", entity.getCurrency());
    assertNull(entity.getId()); // ID is auto-generated
  }

  @Test
  void shouldAllowSettingId() {
    // Given
    PriceEntity entity = new PriceEntity();

    // When
    entity.setId(123L);

    // Then
    assertEquals(123L, entity.getId());
  }

  @Test
  void shouldAllowSettingAllFields() {
    // Given
    PriceEntity entity = new PriceEntity();
    LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 15, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 18, 30);

    // When
    entity.setBrandId(1L);
    entity.setStartDate(startDate);
    entity.setEndDate(endDate);
    entity.setPriceList(2);
    entity.setProductId(35455L);
    entity.setPriority(1);
    entity.setPrice(TEST_PRICE_25_45);
    entity.setCurrency("EUR");

    // Then
    assertEquals(1L, entity.getBrandId());
    assertEquals(startDate, entity.getStartDate());
    assertEquals(endDate, entity.getEndDate());
    assertEquals(2, entity.getPriceList());
    assertEquals(35455L, entity.getProductId());
    assertEquals(1, entity.getPriority());
    assertEquals(TEST_PRICE_25_45, entity.getPrice());
    assertEquals("EUR", entity.getCurrency());
  }
}
