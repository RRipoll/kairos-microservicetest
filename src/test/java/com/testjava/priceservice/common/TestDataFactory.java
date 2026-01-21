package com.testjava.priceservice.common;

import com.testjava.priceservice.domain.model.Price;
import com.testjava.priceservice.domain.model.PriceQuery;
import com.testjava.priceservice.domain.model.PriceResult;
import com.testjava.priceservice.infrastructure.persistence.entity.PriceEntity;
import com.testjava.priceservice.infrastructure.web.dto.PriceResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Factory for creating test data objects consistently across all test layers
 * Provides builder pattern for flexible test data creation
 */
public final class TestDataFactory {

    private TestDataFactory() {
        // Utility class
    }

    // Standard test dates
    public static final LocalDateTime TEST_DATE_2020_06_14_16_00 = LocalDateTime.of(2020, 6, 14, 16, 0);
    public static final LocalDateTime TEST_DATE_2020_06_15_10_00 = LocalDateTime.of(2020, 6, 15, 10, 0);
    public static final LocalDateTime TEST_START_DATE = LocalDateTime.of(2020, 6, 14, 0, 0);
    public static final LocalDateTime TEST_END_DATE = LocalDateTime.of(2020, 12, 31, 23, 59);

    // Standard test values
    public static final Long TEST_PRODUCT_ID = 35455L;
    public static final Long TEST_BRAND_ID = 1L;
    public static final Long TEST_BRAND_ID_2 = 2L;
    public static final String TEST_CURRENCY = "EUR";
    public static final String TEST_CURRENCY_USD = "USD";
    
    // Standard test price values
    public static final BigDecimal TEST_PRICE_35_50 = new BigDecimal("35.50");
    public static final BigDecimal TEST_PRICE_25_45 = new BigDecimal("25.45");
    public static final BigDecimal TEST_PRICE_38_95 = new BigDecimal("38.95");
    public static final BigDecimal TEST_PRICE_30_50 = new BigDecimal("30.50");
    public static final BigDecimal TEST_PRICE_40_50 = new BigDecimal("40.50");
    public static final BigDecimal TEST_PRICE_45_50 = new BigDecimal("45.50");
    public static final BigDecimal TEST_PRICE_PRECISE = new BigDecimal("123.456789");
    
    // Test priority values
    public static final Integer TEST_PRIORITY_0 = 0;
    public static final Integer TEST_PRIORITY_1 = 1;
    public static final Integer TEST_PRIORITY_2 = 2;
    
    // Test price list values
    public static final Integer TEST_PRICE_LIST_1 = 1;
    public static final Integer TEST_PRICE_LIST_2 = 2;
    public static final Integer TEST_PRICE_LIST_3 = 3;
    public static final Integer TEST_PRICE_LIST_4 = 4;
    
    // Test product IDs
    public static final Long TEST_PRODUCT_ID_OTHER = 99999L;
    
    // Performance test constants
    public static final String PERFORMANCE_TEST_URL_BASE = "/api/prices?applicationDate=%s&productId=%d&brandId=%d";
    public static final String PERFORMANCE_DATE_2020_06_14_16_00 = "2020-06-14-16:00:00";
    public static final String PERFORMANCE_DATE_2020_06_14_10_00 = "2020-06-14-10:00:00";
    public static final String PERFORMANCE_DATE_2020_06_15_10_00 = "2020-06-15-10:00:00";
    public static final String PERFORMANCE_DATE_2020_06_16_21_00 = "2020-06-16-21:00:00";
    
    // Date validation test constants
    public static final String VALID_DATE_STRING = "2020-06-14-16:00:00";
    public static final String VALID_EDGE_CASE_DATE = "2020-01-01-00:00:00";
    public static final String INVALID_DATE_MONTH = "2020-13-01-10:00:00";
    
    // ID validation constants
    public static final Long MIN_VALID_ID = 1L;
    public static final Long MAX_VALID_ID = Long.MAX_VALUE;
    protected static final Long[] INVALID_ID_VALUES = {0L, -1L, -999L};
    
    // Validation error messages
    public static final String PRODUCT_ID_NULL_ERROR = "Product ID cannot be null";
    public static final String BRAND_ID_NULL_ERROR = "Brand ID cannot be null";
    public static final String PRODUCT_ID_POSITIVE_ERROR = "Product ID must be positive";
    public static final String BRAND_ID_POSITIVE_ERROR = "Brand ID must be positive";
    
    // Test tags and profiles
    public static final String TEST_PROFILE = "test";
    public static final String PERFORMANCE_TAG = "performance";
    public static final String INTEGRATION_TAG = "integration";
    public static final String UNIT_TAG = "unit";
    
    // Format strings
    public static final String DECIMAL_FORMAT_PATTERN = "%.2f";

    public static PriceQuery createTestQuery() {
        return new PriceQuery(TEST_DATE_2020_06_14_16_00, TEST_PRODUCT_ID, TEST_BRAND_ID);
    }

    public static PriceQuery createTestQuery(LocalDateTime date, Long productId, Long brandId) {
        return new PriceQuery(date, productId, brandId);
    }

    public static Price createTestPrice() {
        return new Price(
            TEST_BRAND_ID,
            TEST_START_DATE,
            TEST_END_DATE,
            1,
            TEST_PRODUCT_ID,
            0,
            TEST_PRICE_35_50,
            TEST_CURRENCY
        );
    }

    public static Price createTestPrice(Integer priceList, Integer priority, BigDecimal price) {
        return new Price(
            TEST_BRAND_ID,
            TEST_START_DATE,
            TEST_END_DATE,
            priceList,
            TEST_PRODUCT_ID,
            priority,
            price,
            TEST_CURRENCY
        );
    }

    public static PriceResult createTestPriceResult() {
        return new PriceResult(
            TEST_PRODUCT_ID,
            TEST_BRAND_ID,
            1,
            TEST_DATE_2020_06_14_16_00,
            TEST_DATE_2020_06_14_16_00,
            TEST_PRICE_35_50
        );
    }

    public static PriceEntity createTestPriceEntity() {
        return new PriceEntity(
            TEST_BRAND_ID,
            TEST_START_DATE,
            TEST_END_DATE,
            1,
            TEST_PRODUCT_ID,
            0,
            TEST_PRICE_35_50,
            TEST_CURRENCY
        );
    }

    public static PriceEntity createTestPriceEntity(Integer priceList, Integer priority, BigDecimal price) {
        return new PriceEntity(
            TEST_BRAND_ID,
            TEST_START_DATE,
            TEST_END_DATE,
            priceList,
            TEST_PRODUCT_ID,
            priority,
            price,
            TEST_CURRENCY
        );
    }

    public static PriceResponse createTestPriceResponse() {
        return new PriceResponse(
            TEST_PRODUCT_ID,
            TEST_BRAND_ID,
            1,
            TEST_DATE_2020_06_14_16_00,
            TEST_DATE_2020_06_14_16_00,
            TEST_PRICE_35_50
        );
    }

    /**
     * Builder for creating test scenarios
     */
    public static class TestScenario {
        public static final TestScenario BASIC_PRICE_LOOKUP = new TestScenario();
        public static final TestScenario MULTIPLE_PRICES_PRIORITY_SELECTION = new TestScenario();
        public static final TestScenario NO_PRICE_FOUND = new TestScenario();
        public static final TestScenario INVALID_DATE = new TestScenario();

        private TestScenario() {}
    }
}