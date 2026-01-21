package com.testjava.priceservice.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PriceQueryTest {

    @Test
    void shouldCreatePriceQueryWithValidParameters() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        
        PriceQuery query = new PriceQuery(applicationDate, productId, brandId);
        
        assertEquals(applicationDate, query.getApplicationDate());
        assertEquals(productId, query.getProductId());
        assertEquals(brandId, query.getBrandId());
    }
}