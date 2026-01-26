package com.testjava.priceservice.domain.strategy;

import java.util.List;
import java.util.Optional;

import com.testjava.priceservice.domain.model.Price;

/**
 * Strategy interface for selecting a price from a list of candidate prices.
 *
 * <p>Implementations define different business rules for selecting the appropriate price when
 * multiple prices match the query criteria.
 */
public interface PriceSelectionStrategy {

  /**
   * Selects a price from the provided list of candidate prices.
   *
   * @param prices the list of candidate prices to select from
   * @return an Optional containing the selected price, or empty if no price can be selected
   * @throws IllegalArgumentException if the prices list is null
   */
  Optional<Price> selectPrice(List<Price> prices);

  /**
   * Returns the name of this strategy for logging and identification purposes.
   *
   * @return the strategy name
   */
  String getStrategyName();
}
