package com.testjava.priceservice.application.usecase;

import static com.testjava.priceservice.common.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.testjava.priceservice.domain.model.PriceQuery;
import com.testjava.priceservice.domain.model.PriceResult;
import com.testjava.priceservice.domain.port.PriceServicePort;
import com.testjava.priceservice.domain.validator.PriceQueryValidator;

class FindPriceUseCaseTest {

  @Mock private PriceServicePort priceService;

  @Mock private PriceQueryValidator queryValidator;

  private FindPriceUseCase findPriceUseCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    findPriceUseCase = new FindPriceUseCase(priceService, queryValidator);
  }

  @Test
  void shouldDelegateToPriceServiceAndReturnResult() {
    // Given
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
    PriceQuery query = new PriceQuery(applicationDate, 35455L, 1L);

    PriceResult expectedResult =
        new PriceResult(
            35455L,
            1L,
            2,
            LocalDateTime.of(2020, 6, 14, 15, 0),
            LocalDateTime.of(2020, 6, 14, 18, 30),
            TEST_PRICE_25_45,
            TEST_CURRENCY);

    when(priceService.findApplicablePrice(query)).thenReturn(Optional.of(expectedResult));

    // When
    Optional<PriceResult> result = findPriceUseCase.execute(query);

    // Then
    assertTrue(result.isPresent());
    assertEquals(expectedResult, result.get());

    verify(priceService).findApplicablePrice(query);
  }

  @Test
  void shouldReturnEmptyWhenPriceServiceReturnsEmpty() {
    // Given
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
    PriceQuery query = new PriceQuery(applicationDate, 35455L, 1L);

    when(priceService.findApplicablePrice(query)).thenReturn(Optional.empty());

    // When
    Optional<PriceResult> result = findPriceUseCase.execute(query);

    // Then
    assertFalse(result.isPresent());

    verify(priceService).findApplicablePrice(query);
  }

  @Test
  void shouldCallPriceServiceWithCorrectParameters() {
    // Given
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 10, 0);
    Long productId = 35455L;
    Long brandId = 1L;
    PriceQuery query = new PriceQuery(applicationDate, productId, brandId);

    when(priceService.findApplicablePrice(any(PriceQuery.class))).thenReturn(Optional.empty());

    // When
    findPriceUseCase.execute(query);

    // Then
    verify(priceService)
        .findApplicablePrice(
            argThat(
                q ->
                    q.getApplicationDate().equals(applicationDate)
                        && q.getProductId().equals(productId)
                        && q.getBrandId().equals(brandId)));
  }
}
