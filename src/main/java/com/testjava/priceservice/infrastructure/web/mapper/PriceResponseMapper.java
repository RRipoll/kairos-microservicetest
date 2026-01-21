package com.testjava.priceservice.infrastructure.web.mapper;

import com.testjava.priceservice.domain.model.PriceResult;
import com.testjava.priceservice.infrastructure.web.dto.PriceResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper responsible for converting domain PriceResult objects to web layer PriceResponse DTOs.
 * This class has the single responsibility of handling the transformation between 
 * domain and presentation layer data structures.
 */
@Component
public class PriceResponseMapper {

    /**
     * Maps a domain PriceResult to a web layer PriceResponse.
     * 
     * @param result the domain price result to map
     * @return PriceResponse DTO for the web layer
     * @throws IllegalArgumentException if result is null
     */
    public PriceResponse mapToResponse(PriceResult result) {
        if (result == null) {
            throw new IllegalArgumentException("PriceResult cannot be null");
        }
        
        return new PriceResponse(
            result.getProductId(),
            result.getBrandId(),
            result.getPriceList(),
            result.getStartDate(),
            result.getEndDate(),
            result.getPrice()
        );
    }
}