package com.testjava.priceservice.domain.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.testjava.priceservice.domain.model.Price;

@DisplayName("HighestPriorityStrategy Unit Tests")
class HighestPriorityStrategyTest {

  private HighestPriorityStrategy strategy;

  @BeforeEach
  void setUp() {
    strategy = new HighestPriorityStrategy();
  }

  @Test
  @DisplayName("Should return empty Optional when price list is empty")
  void shouldReturnEmptyWhenPriceListIsEmpty() {
    // Given
    List<Price> emptyList = new ArrayList<>();

    // When
    Optional<Price> result = strategy.selectPrice(emptyList);

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when price list is null")
  void shouldThrowExceptionWhenPriceListIsNull() {
    // When & Then
    assertThatThrownBy(() -> strategy.selectPrice(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Price list cannot be null");
  }

  @Test
  @DisplayName("Should return the single price when list contains only one price")
  void shouldReturnSinglePriceWhenListContainsOne() {
    // Given
    Price singlePrice = createPrice(1L, 1, new BigDecimal("35.50"));
    List<Price> prices = List.of(singlePrice);

    // When
    Optional<Price> result = strategy.selectPrice(prices);

    // Then
    assertThat(result).isPresent().contains(singlePrice);
  }

  @Test
  @DisplayName("Should return price with highest priority when multiple prices exist")
  void shouldReturnHighestPriorityPrice() {
    // Given
    Price lowPriority = createPrice(1L, 0, new BigDecimal("35.50"));
    Price mediumPriority = createPrice(2L, 1, new BigDecimal("25.45"));
    Price highPriority = createPrice(3L, 2, new BigDecimal("30.50"));
    List<Price> prices = Arrays.asList(lowPriority, highPriority, mediumPriority);

    // When
    Optional<Price> result = strategy.selectPrice(prices);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().getPriority()).isEqualTo(2);
    assertThat(result.get().getPriceList()).isEqualTo(3L);
  }

  @Test
  @DisplayName("Should return deterministically when multiple prices have same priority")
  void shouldReturnDeterministicallyWhenSamePriority() {
    // Given
    Price price1 = createPrice(1L, 1, new BigDecimal("35.50"));
    Price price2 = createPrice(2L, 1, new BigDecimal("25.45"));
    Price price3 = createPrice(3L, 1, new BigDecimal("30.50"));
    List<Price> prices = Arrays.asList(price1, price2, price3);

    // When
    Optional<Price> result1 = strategy.selectPrice(prices);
    Optional<Price> result2 = strategy.selectPrice(prices);

    // Then
    assertThat(result1).isPresent();
    assertThat(result2).isPresent();
    // Should return the same result on multiple calls
    assertThat(result1.get()).isEqualTo(result2.get());
    // Should have priority 1
    assertThat(result1.get().getPriority()).isEqualTo(1);
  }

  @Test
  @DisplayName("Should handle negative priorities correctly")
  void shouldHandleNegativePriorities() {
    // Given
    Price negativePriority = createPrice(1L, -1, new BigDecimal("35.50"));
    Price zeroPriority = createPrice(2L, 0, new BigDecimal("25.45"));
    Price positivePriority = createPrice(3L, 1, new BigDecimal("30.50"));
    List<Price> prices = Arrays.asList(negativePriority, zeroPriority, positivePriority);

    // When
    Optional<Price> result = strategy.selectPrice(prices);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().getPriority()).isEqualTo(1);
    assertThat(result.get().getPriceList()).isEqualTo(3L);
  }

  @Test
  @DisplayName("Should return correct strategy name")
  void shouldReturnCorrectStrategyName() {
    // When
    String strategyName = strategy.getStrategyName();

    // Then
    assertThat(strategyName).isEqualTo("HighestPriorityStrategy");
  }

  private Price createPrice(Long priceList, int priority, BigDecimal price) {
    return new Price(
        1L, // brandId
        LocalDateTime.of(2020, 6, 14, 0, 0, 0), // startDate
        LocalDateTime.of(2020, 12, 31, 23, 59, 59), // endDate
        priceList.intValue(), // priceList
        35455L, // productId
        priority, // priority
        price, // priceValue
        "EUR" // currency
        );
  }
}
