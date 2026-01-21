package com.testjava.priceservice.domain.common;

/**
 * Centralized validation error messages to avoid duplication.
 */
public final class ValidationMessages {

    private ValidationMessages() {
        // Utility class
    }

    // Query validation messages
    public static final String PRICE_QUERY_NULL = "PriceQuery cannot be null";
    public static final String APPLICATION_DATE_REQUIRED = "Application date is required";
    public static final String PRODUCT_ID_REQUIRED = "Product ID is required";
    public static final String BRAND_ID_REQUIRED = "Brand ID is required";
    public static final String PRODUCT_ID_POSITIVE = "Product ID must be positive";
    public static final String BRAND_ID_POSITIVE = "Brand ID must be positive";

    // Domain model validation messages
    public static final String PRICE_CANNOT_BE_NULL = "Price cannot be null";
    public static final String PRODUCT_ID_CANNOT_BE_NULL = "Product ID cannot be null";
    public static final String BRAND_ID_CANNOT_BE_NULL = "Brand ID cannot be null";

    // Date validation messages
    public static final String INVALID_DATE_FORMAT_VALUES = "Invalid date format or values";
    public static final String INVALID_MONTH_VALUE = "Invalid month value: ";
    public static final String INVALID_DAY_VALUE = "Invalid day value: ";
    public static final String INVALID_HOUR_VALUE = "Invalid hour value: ";
    public static final String INVALID_MINUTE_VALUE = "Invalid minute value: ";
    public static final String INVALID_SECOND_VALUE = "Invalid second value: ";

    // Exception format messages
    public static final String INVALID_PRICE_QUERY_FORMAT = "Invalid price query - %s: %s (%s)";
    public static final String PRICE_NOT_FOUND_FORMAT = "No applicable price found for product %d, brand %d on date %s";
}