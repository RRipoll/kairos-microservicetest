package com.testjava.priceservice.performance;

import static com.testjava.priceservice.common.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.testjava.priceservice.common.TestCategories;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(TEST_PROFILE)
@Tag(PERFORMANCE_TAG)
@DisplayName("Performance Tests - Price Service Load Testing")
@Disabled("Performance tests should be run separately")
class PriceServicePerformanceTest implements TestCategories.PerformanceTest {

  private static final Logger log = LoggerFactory.getLogger(PriceServicePerformanceTest.class);

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  private String createUrl(String path) {
    return "http://localhost:" + port + path;
  }

  @Test
  @DisplayName("Should handle 100 concurrent requests within acceptable time")
  void shouldHandle100ConcurrentRequestsWithinAcceptableTime() throws Exception {
    // Given
    String url =
        createUrl(
            String.format(
                PERFORMANCE_TEST_URL_BASE,
                PERFORMANCE_DATE_2020_06_14_16_00,
                TEST_PRODUCT_ID,
                TEST_BRAND_ID));
    int numberOfRequests = 100;
    ExecutorService executor = Executors.newFixedThreadPool(10);

    // When
    Instant start = Instant.now();

    @SuppressWarnings("unchecked")
    CompletableFuture<ResponseEntity<String>>[] futures =
        IntStream.range(0, numberOfRequests)
            .mapToObj(
                i ->
                    CompletableFuture.supplyAsync(
                        () -> restTemplate.getForEntity(url, String.class), executor))
            .toArray(CompletableFuture[]::new);

    CompletableFuture.allOf(futures).join();

    Instant end = Instant.now();
    Duration duration = Duration.between(start, end);

    // Then
    assertThat(duration.toMillis()).isLessThan(5000); // Should complete within 5 seconds

    // Verify all requests succeeded
    for (CompletableFuture<ResponseEntity<String>> future : futures) {
      ResponseEntity<String> response = future.get();
      assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    executor.shutdown();
    log.info(
        "Completed {} requests in {} ms (avg: {} ms/req)",
        numberOfRequests,
        duration.toMillis(),
        String.format(DECIMAL_FORMAT_PATTERN, duration.toMillis() / (double) numberOfRequests));
  }

  @Test
  @DisplayName("Should maintain response time under load")
  void shouldMaintainResponseTimeUnderLoad() {
    // Given
    String url =
        createUrl(
            String.format(
                PERFORMANCE_TEST_URL_BASE,
                PERFORMANCE_DATE_2020_06_14_16_00,
                TEST_PRODUCT_ID,
                TEST_BRAND_ID));
    int warmupRequests = 10;
    int measurementRequests = 50;

    // Warmup
    for (int i = 0; i < warmupRequests; i++) {
      restTemplate.getForEntity(url, String.class);
    }

    // When - Measure response times
    long totalTime = 0;
    long maxTime = 0;

    for (int i = 0; i < measurementRequests; i++) {
      Instant start = Instant.now();
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
      Instant end = Instant.now();

      long requestTime = Duration.between(start, end).toMillis();
      totalTime += requestTime;
      maxTime = Math.max(maxTime, requestTime);

      assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    // Then
    double averageTime = totalTime / (double) measurementRequests;

    assertThat(averageTime).isLessThan(100); // Average should be under 100ms
    assertThat(maxTime).isLessThan(500); // No single request should take over 500ms

    log.info(
        "Average response time: {} ms, Max: {} ms",
        String.format(DECIMAL_FORMAT_PATTERN, averageTime),
        maxTime);
  }

  @Test
  @DisplayName("Should handle mixed load scenarios")
  void shouldHandleMixedLoadScenarios() throws Exception {
    // Given
    String[] urls = {
      createUrl(
          String.format(
              PERFORMANCE_TEST_URL_BASE,
              PERFORMANCE_DATE_2020_06_14_10_00,
              TEST_PRODUCT_ID,
              TEST_BRAND_ID)),
      createUrl(
          String.format(
              PERFORMANCE_TEST_URL_BASE,
              PERFORMANCE_DATE_2020_06_14_16_00,
              TEST_PRODUCT_ID,
              TEST_BRAND_ID)),
      createUrl(
          String.format(
              PERFORMANCE_TEST_URL_BASE,
              PERFORMANCE_DATE_2020_06_15_10_00,
              TEST_PRODUCT_ID,
              TEST_BRAND_ID)),
      createUrl(
          String.format(
              PERFORMANCE_TEST_URL_BASE,
              PERFORMANCE_DATE_2020_06_16_21_00,
              TEST_PRODUCT_ID,
              TEST_BRAND_ID))
    };

    int requestsPerUrl = 25;
    ExecutorService executor = Executors.newFixedThreadPool(8);

    // When
    Instant start = Instant.now();

    @SuppressWarnings("unchecked")
    CompletableFuture<ResponseEntity<String>>[] futures =
        IntStream.range(0, urls.length)
            .boxed()
            .flatMap(
                urlIndex ->
                    IntStream.range(0, requestsPerUrl)
                        .mapToObj(
                            i ->
                                CompletableFuture.supplyAsync(
                                    () -> restTemplate.getForEntity(urls[urlIndex], String.class),
                                    executor)))
            .toArray(CompletableFuture[]::new);

    CompletableFuture.allOf(futures).join();

    Instant end = Instant.now();
    Duration duration = Duration.between(start, end);

    // Then
    assertThat(duration.toMillis()).isLessThan(8000); // Should complete within 8 seconds

    // Verify all requests succeeded
    for (CompletableFuture<ResponseEntity<String>> future : futures) {
      ResponseEntity<String> response = future.get();
      assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    executor.shutdown();
    log.info(
        "Mixed load test: {} requests completed in {} ms", futures.length, duration.toMillis());
  }
}
