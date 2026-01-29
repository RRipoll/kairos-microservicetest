package com.testjava.priceservice.infrastructure.web.dto;

import static com.testjava.priceservice.common.TestDataFactory.TEST_PRICE_25_45;
import static com.testjava.priceservice.common.TestDataFactory.TEST_PRICE_35_50;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class PriceResponseTest {

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  void shouldCreatePriceResponseWithAllFields() {
    // Given
    LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 15, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 18, 30);

    // When
    PriceResponse response =
        new PriceResponse(35455L, 1L, 2, startDate, endDate, TEST_PRICE_25_45, "EUR");

    // Then
    assertEquals(35455L, response.getProductId());
    assertEquals(1L, response.getBrandId());
    assertEquals(2, response.getPriceList());
    assertEquals(startDate, response.getStartDate());
    assertEquals(endDate, response.getEndDate());
    assertEquals(TEST_PRICE_25_45, response.getPrice());
  }

  @Test
  void shouldAllowSettingAllFields() {
    // Given
    PriceResponse response = new PriceResponse();
    LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);

    // When
    response.setProductId(35455L);
    response.setBrandId(1L);
    response.setPriceList(1);
    response.setStartDate(startDate);
    response.setEndDate(endDate);
    response.setPrice(TEST_PRICE_35_50);
    response.setCurrency("EUR");

    // Then
    assertEquals(35455L, response.getProductId());
    assertEquals(1L, response.getBrandId());
    assertEquals(1, response.getPriceList());
    assertEquals(startDate, response.getStartDate());
    assertEquals(endDate, response.getEndDate());
    assertEquals(TEST_PRICE_35_50, response.getPrice());
    assertEquals("EUR", response.getCurrency());
  }

  @Test
  void shouldSerializeToJsonCorrectly() throws Exception {
    // Given
    LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 15, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 18, 30);

    PriceResponse response =
        new PriceResponse(35455L, 1L, 2, startDate, endDate, TEST_PRICE_25_45, "EUR");

    // When
    String json = objectMapper.writeValueAsString(response);

    // Then
    assertNotNull(json);
    assertTrue(json.contains("\"productId\":35455"));
    assertTrue(json.contains("\"brandId\":1"));
    assertTrue(json.contains("\"priceList\":2"));
    assertTrue(json.contains("\"price\":25.45"));
  }
}
