package com.testjava.priceservice.domain.model;

import static com.testjava.priceservice.common.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class PriceResultTest {

  @Test
  void shouldCreatePriceResultWithValidParameters() {
    LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);

    PriceResult result = new PriceResult(35455L, 1L, 1, startDate, endDate, TEST_PRICE_35_50);

    assertEquals(35455L, result.getProductId());
    assertEquals(1L, result.getBrandId());
    assertEquals(1, result.getPriceList());
    assertEquals(startDate, result.getStartDate());
    assertEquals(endDate, result.getEndDate());
    assertEquals(TEST_PRICE_35_50, result.getPrice());
  }
}
