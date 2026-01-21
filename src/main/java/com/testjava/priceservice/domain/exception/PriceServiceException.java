package com.testjava.priceservice.domain.exception;

/**
 * Base exception for all domain-level price service exceptions.
 * This exception represents business rule violations and domain errors.
 */
public class PriceServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PriceServiceException(final String message) {
        super(message);
    }

    public PriceServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PriceServiceException(final Throwable cause) {
        super(cause);
    }
}