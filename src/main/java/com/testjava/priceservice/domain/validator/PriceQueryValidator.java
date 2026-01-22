package com.testjava.priceservice.domain.validator;

import org.springframework.stereotype.Component;

import com.testjava.priceservice.domain.common.ValidationMessages;
import com.testjava.priceservice.domain.common.ValidationRules;
import com.testjava.priceservice.domain.model.PriceQuery;

/**
 * Validator responsible for validating PriceQuery domain objects. This class has the single
 * responsibility of ensuring PriceQuery objects meet domain business rules and constraints.
 */
@Component
public class PriceQueryValidator {

  /**
   * Validates a PriceQuery object according to domain business rules.
   *
   * @param query the PriceQuery to validate
   * @throws IllegalArgumentException if validation fails
   */
  public void validate(PriceQuery query) {
    if (query == null) {
      throw new IllegalArgumentException(ValidationMessages.PRICE_QUERY_NULL);
    }

    if (query.getApplicationDate() == null) {
      throw new IllegalArgumentException(ValidationMessages.APPLICATION_DATE_REQUIRED);
    }

    if (query.getProductId() == null) {
      throw new IllegalArgumentException(ValidationMessages.PRODUCT_ID_REQUIRED);
    }

    if (query.getBrandId() == null) {
      throw new IllegalArgumentException(ValidationMessages.BRAND_ID_REQUIRED);
    }

    // Domain business rules
    if (query.getProductId() <= ValidationRules.MIN_POSITIVE_VALUE) {
      throw new IllegalArgumentException(ValidationMessages.PRODUCT_ID_POSITIVE);
    }

    if (query.getBrandId() <= ValidationRules.MIN_POSITIVE_VALUE) {
      throw new IllegalArgumentException(ValidationMessages.BRAND_ID_POSITIVE);
    }
  }

  /**
   * Checks if a PriceQuery is valid without throwing exceptions.
   *
   * @param query the PriceQuery to check
   * @return true if valid, false otherwise
   */
  public boolean isValid(PriceQuery query) {
    try {
      validate(query);
      return true;
    } catch (IllegalArgumentException ignored) {
      return false;
    }
  }
}
