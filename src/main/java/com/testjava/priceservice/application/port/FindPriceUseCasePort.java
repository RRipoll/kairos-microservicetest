package com.testjava.priceservice.application.port;

import com.testjava.priceservice.domain.model.PriceQuery;
import com.testjava.priceservice.domain.model.PriceResult;

import java.util.Optional;

/**
 * Port interface for price finding use cases.
 * This interface decouples the infrastructure layer from the concrete use case implementation,
 * allowing for different implementations and better testability.
 */
public interface FindPriceUseCasePort {
    
    /**
     * Executes the price finding use case with the given query parameters.
     * 
     * @param query the price query containing search criteria
     * @return Optional containing the price result if found, empty otherwise
     */
    Optional<PriceResult> execute(PriceQuery query);
}