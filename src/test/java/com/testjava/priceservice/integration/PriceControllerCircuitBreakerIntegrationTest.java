package com.testjava.priceservice.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.testjava.priceservice.application.port.FindPriceUseCasePort;
import com.testjava.priceservice.common.TestCategories;
import com.testjava.priceservice.domain.model.PriceResult;
import com.testjava.priceservice.infrastructure.common.DateFormats;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Integration Tests - PriceController Circuit Breaker")
class PriceControllerCircuitBreakerIntegrationTest implements TestCategories.IntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  @MockitoBean private FindPriceUseCasePort findPriceUseCase;

  @Autowired private CircuitBreakerRegistry circuitBreakerRegistry;

  private static final String API_URL =
      "/api/prices?applicationDate={date}&productId={productId}&brandId={brandId}";
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern(DateFormats.API_DATE_TIME_FORMAT);
  private static final String TEST_USER = "user";
  private static final String TEST_PASSWORD = "test";

  @BeforeEach
  void setUp() {
    // Reset the circuit breaker before each test
    circuitBreakerRegistry.circuitBreaker("priceService").reset();
  }

  @Test
  @DisplayName("Should stay CLOSED when calls are successful")
  void shouldStayClosedWhenCallsAreSuccessful() {
    // Given
    when(findPriceUseCase.execute(any())).thenReturn(java.util.Optional.empty());

    // When
    for (int i = 0; i < 5; i++) {
      makeCall();
    }

    // Then
    assertThat(circuitBreakerRegistry.circuitBreaker("priceService").getState())
        .isEqualTo(io.github.resilience4j.circuitbreaker.CircuitBreaker.State.CLOSED);
    verify(findPriceUseCase, times(5)).execute(any());
  }

  @Test
  @DisplayName("Should open circuit after failure threshold is reached")
  void shouldOpenCircuitAfterFailureThreshold() {
    // Given
    // Config: slidingWindowSize: 10, minimumNumberOfCalls: 5, failureRateThreshold:
    // 50
    when(findPriceUseCase.execute(any())).thenThrow(new RuntimeException("Service Failure"));

    // When - Make 5 calls (minimumNumberOfCalls)
    for (int i = 0; i < 5; i++) {
      ResponseEntity<String> response = makeCall();
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    // Then - Circuit should be OPEN now
    assertThat(circuitBreakerRegistry.circuitBreaker("priceService").getState())
        .isEqualTo(io.github.resilience4j.circuitbreaker.CircuitBreaker.State.OPEN);

    // When - Make another call while OPEN
    ResponseEntity<String> response = makeCall();

    // Then - Should return 503 without calling the service
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    verify(findPriceUseCase, times(5)).execute(any()); // Still only 5 calls made to the service
  }

  @Test
  @DisplayName("Should transition from OPEN to HALF_OPEN and then CLOSED after success")
  void shouldTransitionOpenToHalfOpenToClosed() throws InterruptedException {
    // 1. Force circuit to OPEN
    when(findPriceUseCase.execute(any())).thenThrow(new RuntimeException("Service Failure"));
    for (int i = 0; i < 5; i++) {
      makeCall();
    }
    assertThat(circuitBreakerRegistry.circuitBreaker("priceService").getState())
        .isEqualTo(io.github.resilience4j.circuitbreaker.CircuitBreaker.State.OPEN);

    // 2. Wait for waitDurationInOpenState (5s - according to application.yml)
    // Note: In real tests we might use a shorter duration or TimeLimiter but let's
    // wait for simplicity here
    // or we could manually move it to half-open if needed, but let's test real
    // time.
    Thread.sleep(6000);

    // 3. Make a call - should be HALF_OPEN
    // When service starts working again - return successful price results
    reset(findPriceUseCase);
    PriceResult mockResult =
        new PriceResult(
            35455L,
            1L,
            1,
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().plusDays(1),
            new BigDecimal("35.50"),
            "EUR");
    when(findPriceUseCase.execute(any())).thenReturn(java.util.Optional.of(mockResult));

    ResponseEntity<String> response = makeCall();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    // Should be HALF_OPEN now
    assertThat(circuitBreakerRegistry.circuitBreaker("priceService").getState())
        .isEqualTo(io.github.resilience4j.circuitbreaker.CircuitBreaker.State.HALF_OPEN);

    // 4. Meet permittedNumberOfCallsInHalfOpenState (3)
    makeCall();
    makeCall();

    // 5. Should be CLOSED now
    assertThat(circuitBreakerRegistry.circuitBreaker("priceService").getState())
        .isEqualTo(io.github.resilience4j.circuitbreaker.CircuitBreaker.State.CLOSED);
  }

  private ResponseEntity<String> makeCall() {
    String date = LocalDateTime.now().format(FORMATTER);
    return restTemplate
        .withBasicAuth(TEST_USER, TEST_PASSWORD)
        .getForEntity(API_URL, String.class, date, 35455L, 1L);
  }
}
