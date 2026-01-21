package com.testjava.priceservice.domain.model;
import static com.testjava.priceservice.common.TestDataFactory.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    @Test
    void shouldCreatePriceWithValidParameters() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);
        
        Price price = new Price(
            1L, startDate, endDate, 1, 35455L, 0, 
            TEST_PRICE_35_50, "EUR"
        );
        
        assertEquals(1L, price.getBrandId());
        assertEquals(startDate, price.getStartDate());
        assertEquals(endDate, price.getEndDate());
        assertEquals(1, price.getPriceList());
        assertEquals(35455L, price.getProductId());
        assertEquals(0, price.getPriority());
        assertEquals(TEST_PRICE_35_50, price.getPrice());
        assertEquals(TEST_PRICE_35_50, price.getPriceValue()); // Test the Lombok-generated getter
        assertEquals("EUR", price.getCurrency());
    }

    @Test
    void shouldReturnTrueWhenDateIsWithinRange() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 23, 59);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 12, 0);
        
        Price price = new Price(
            1L, startDate, endDate, 1, 35455L, 0, 
            TEST_PRICE_35_50, "EUR"
        );
        
        assertTrue(price.isApplicableAt(applicationDate));
    }

    @Test
    void shouldReturnTrueWhenDateEqualsStartDate() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 23, 59);
        
        Price price = new Price(
            1L, startDate, endDate, 1, 35455L, 0, 
            TEST_PRICE_35_50, "EUR"
        );
        
        assertTrue(price.isApplicableAt(startDate));
    }

    @Test
    void shouldReturnTrueWhenDateEqualsEndDate() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 23, 59);
        
        Price price = new Price(
            1L, startDate, endDate, 1, 35455L, 0, 
            TEST_PRICE_35_50, "EUR"
        );
        
        assertTrue(price.isApplicableAt(endDate));
    }

    @Test
    void shouldReturnFalseWhenDateIsBeforeStartDate() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 23, 59);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 13, 23, 59);
        
        Price price = new Price(
            1L, startDate, endDate, 1, 35455L, 0, 
            TEST_PRICE_35_50, "EUR"
        );
        
        assertFalse(price.isApplicableAt(applicationDate));
    }

    @Test
    void shouldReturnFalseWhenDateIsAfterEndDate() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 23, 59);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 0, 0);
        
        Price price = new Price(
            1L, startDate, endDate, 1, 35455L, 0, 
            TEST_PRICE_35_50, "EUR"
        );
        
        assertFalse(price.isApplicableAt(applicationDate));
    }
}