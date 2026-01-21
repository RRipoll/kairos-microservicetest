package com.testjava.priceservice.domain.exception;

import com.testjava.priceservice.domain.common.ValidationMessages;

/**
 * Exception thrown when a price query violates domain business rules.
 * This exception represents invalid input at the domain level.
 */
public class InvalidPriceQueryException extends PriceServiceException {

    private static final long serialVersionUID = 1L;

    private final String field;
    private final Object value;

    public InvalidPriceQueryException(final String field, final Object value, final String message) {
        super(String.format(ValidationMessages.INVALID_PRICE_QUERY_FORMAT, field, value, message));
        this.field = field;
        this.value = value;
    }

    public InvalidPriceQueryException(final String field, final Object value, final String message, final Throwable cause) {
        super(String.format(ValidationMessages.INVALID_PRICE_QUERY_FORMAT, field, value, message), cause);
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }
}