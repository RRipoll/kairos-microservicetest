package com.testjava.priceservice.domain.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.testjava.priceservice.domain.model.Price;

import lombok.extern.slf4j.Slf4j;

/**
 * Strategy that selects the price with the highest priority value.
 *
 * <p>When multiple prices match the query criteria, this strategy selects the one with the highest
 * priority number. This is the default business rule for the e-commerce price management system.
 */
@Slf4j
@Component
public class HighestPriorityStrategy implements PriceSelectionStrategy {

  @Override
  public Optional<Price> selectPrice(final List<Price> prices) {
    if (prices == null) {
      throw new IllegalArgumentException("Price list cannot be null");
    }

    if (prices.isEmpty()) {
      log.debug("Empty price list provided, returning empty result");
      return Optional.empty();
    }

    log.debug("Selecting price with highest priority from {} candidates", prices.size());

    return prices.stream().max(Comparator.comparingInt(Price::getPriority));
  }

  @Override
  public String getStrategyName() {
    return "HighestPriorityStrategy";
  }
}
