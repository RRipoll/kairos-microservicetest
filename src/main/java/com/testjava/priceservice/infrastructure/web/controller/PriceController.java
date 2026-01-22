package com.testjava.priceservice.infrastructure.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.testjava.priceservice.application.port.FindPriceUseCasePort;
import com.testjava.priceservice.domain.model.PriceQuery;
import com.testjava.priceservice.domain.model.PriceResult;
import com.testjava.priceservice.infrastructure.web.dto.PriceResponse;
import com.testjava.priceservice.infrastructure.web.dto.ErrorResponse;
import com.testjava.priceservice.infrastructure.web.mapper.PriceResponseMapper;
import org.springframework.format.annotation.DateTimeFormat;
import com.testjava.priceservice.infrastructure.common.DateFormats;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
@Tag(name = "Price API", description = "API for e-commerce price consultation with time-based validity and priority rules")
public class PriceController {

    private final FindPriceUseCasePort findPriceUseCase;
    private final PriceResponseMapper responseMapper;

    @GetMapping
    @Operation(summary = "Get applicable price for a product", description = "Returns the applicable price for a specific product of a brand at a given date. "
            +
            "Automatically selects the price with the highest priority when multiple prices are valid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Price found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceResponse.class))),
            @ApiResponse(responseCode = "404", description = "No applicable price found for the given criteria", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters (e.g., invalid date format)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable due to circuit breaker", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @CircuitBreaker(name = "priceService", fallbackMethod = "fallbackGetPrice")
    public ResponseEntity<PriceResponse> getPrice(
            @Parameter(name = "applicationDate", description = "Date and time for price application in format yyyy-MM-dd-HH:mm:ss", example = "2020-06-14-16:00:00", required = true) @RequestParam("applicationDate") @DateTimeFormat(pattern = DateFormats.API_DATE_TIME_FORMAT) LocalDateTime date,

            @Parameter(name = "productId", description = "Product identifier", example = "35455", required = true) @RequestParam("productId") Long productId,

            @Parameter(name = "brandId", description = "Brand identifier (1 = ZARA)", example = "1", required = true) @RequestParam("brandId") Long brandId) {

        log.info("Received price request - date: {}, productId: {}, brandId: {}",
                date, productId, brandId);

        // Create query
        PriceQuery query = new PriceQuery(date, productId, brandId);

        // Execute use case
        Optional<PriceResult> result = findPriceUseCase.execute(query);

        // Handle response
        return result
                .map(responseMapper::mapToResponse)
                .map(response -> {
                    log.info("Price request successful - returning price: {}", response.getPrice());
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    log.info("Price request completed - no price found for given criteria");
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Fallback method for getPrice when the circuit is open or an error occurs.
     */
    public ResponseEntity<PriceResponse> fallbackGetPrice(LocalDateTime date, Long productId, Long brandId,
            Exception e) {
        log.error(
                "Circuit breaker fallback triggered for price request - date: {}, productId: {}, brandId: {}. Error: {}",
                date, productId, brandId, e.getMessage());

        // Return 503 Service Unavailable when the system is under pressure or failing
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

}