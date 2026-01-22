package com.testjava.priceservice.infrastructure.web.controller;

import com.testjava.priceservice.application.port.FindPriceUseCasePort;
import com.testjava.priceservice.infrastructure.web.mapper.PriceResponseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceControllerResilienceTest {

    @Mock
    private FindPriceUseCasePort findPriceUseCase;

    @Mock
    private PriceResponseMapper responseMapper;

    @InjectMocks
    private PriceController priceController;

    @Test
    void whenUseCaseThrowsException_thenFallbackIsTriggered() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Long productId = 35455L;
        Long brandId = 1L;
        Exception exception = new RuntimeException("Service failure");

        // The fallback method is called directly in our test for verification
        // because the @CircuitBreaker annotation requires a Spring context with AOP
        // to be triggered automatically. Here we verify the logic of the fallback
        // method itself.

        // Act
        ResponseEntity<?> response = priceController.fallbackGetPrice(now, productId, brandId, exception);

        // Assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }
}
