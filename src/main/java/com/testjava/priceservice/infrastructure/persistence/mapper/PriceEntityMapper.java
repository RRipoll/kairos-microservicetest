package com.testjava.priceservice.infrastructure.persistence.mapper;

import com.testjava.priceservice.domain.model.Price;
import com.testjava.priceservice.infrastructure.persistence.entity.PriceEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper responsible for converting between persistence entities and domain objects.
 * This class has the single responsibility of handling the transformation between
 * infrastructure and domain layer data structures.
 */
@Component
public class PriceEntityMapper {

    /**
     * Maps a persistence PriceEntity to a domain Price object.
     * 
     * @param entity the persistence entity to map
     * @return Price domain object
     * @throws IllegalArgumentException if entity is null
     */
    public Price mapToDomain(PriceEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("PriceEntity cannot be null");
        }
        
        return new Price(
            entity.getBrandId(),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.getPriceList(),
            entity.getProductId(),
            entity.getPriority(),
            entity.getPrice(),
            entity.getCurrency()
        );
    }

    /**
     * Maps a domain Price object to a persistence PriceEntity.
     * 
     * @param price the domain price to map
     * @return PriceEntity for persistence
     * @throws IllegalArgumentException if price is null
     */
    public PriceEntity mapToEntity(Price price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        
        return new PriceEntity(
            price.getBrandId(),
            price.getStartDate(),
            price.getEndDate(),
            price.getPriceList(),
            price.getProductId(),
            price.getPriority(),
            price.getPrice(),
            price.getCurrency()
        );
    }
}