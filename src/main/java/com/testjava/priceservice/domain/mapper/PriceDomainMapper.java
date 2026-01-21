package com.testjava.priceservice.domain.mapper;

import com.testjava.priceservice.domain.common.ValidationMessages;
import com.testjava.priceservice.domain.model.Price;
import com.testjava.priceservice.domain.model.PriceResult;
import org.springframework.stereotype.Component;

/**
 * Mapper responsible for transformations within the domain layer.
 * This class has the single responsibility of converting between different
 * domain objects while maintaining domain integrity.
 */
@Component
public class PriceDomainMapper {

    /**
     * Maps a domain Price entity to a PriceResult.
     * This transformation represents the result of a successful price query.
     * 
     * @param price the domain price entity to map
     * @return PriceResult representing the query result
     * @throws IllegalArgumentException if price is null
     */
    public PriceResult mapToResult(Price price) {
        if (price == null) {
            throw new IllegalArgumentException(ValidationMessages.PRICE_CANNOT_BE_NULL);
        }
        
        return new PriceResult(
            price.getProductId(),
            price.getBrandId(),
            price.getPriceList(),
            price.getStartDate(),
            price.getEndDate(),
            price.getPrice()
        );
    }
}