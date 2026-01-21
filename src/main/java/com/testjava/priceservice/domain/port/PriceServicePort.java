package com.testjava.priceservice.domain.port;

import com.testjava.priceservice.domain.model.PriceQuery;
import com.testjava.priceservice.domain.model.PriceResult;

import java.util.Optional;

/**
 * Port interface for price domain services.
 * This interface allows for different implementations of price finding logic
 * and facilitates testing by providing a contract for price service operations.
 */
public interface PriceServicePort {
    
    /**
     * Finds the applicable price for the given query criteria.
     * Applies business rules for price selection including priority resolution
     * and temporal validity checking.
     * 
     * @param query the price query with search criteria
     * @return Optional containing the applicable price result, empty if no price matches
     */
    Optional<PriceResult> findApplicablePrice(PriceQuery query);
}