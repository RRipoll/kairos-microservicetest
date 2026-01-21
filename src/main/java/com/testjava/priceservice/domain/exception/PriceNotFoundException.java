package com.testjava.priceservice.domain.exception;

import com.testjava.priceservice.domain.common.ValidationMessages;

/**
 * Exception thrown when no applicable price is found for the given criteria.
 * This exception represents a business rule violation in the domain layer.
 */
public class PriceNotFoundException extends PriceServiceException {
    
    private static final long serialVersionUID = 1L;
    
    private final Long productId;
    private final Long brandId;
    private final String applicationDate;

    public PriceNotFoundException(final Long productId, final Long brandId, final String applicationDate) {
        super(String.format(ValidationMessages.PRICE_NOT_FOUND_FORMAT, 
              productId, brandId, applicationDate));
        this.productId = productId;
        this.brandId = brandId;
        this.applicationDate = applicationDate;
    }

    public PriceNotFoundException(final Long productId, final Long brandId, final String applicationDate, final Throwable cause) {
        super(String.format(ValidationMessages.PRICE_NOT_FOUND_FORMAT, 
              productId, brandId, applicationDate), cause);
        this.productId = productId;
        this.brandId = brandId;
        this.applicationDate = applicationDate;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public String getApplicationDate() {
        return applicationDate;
    }
}