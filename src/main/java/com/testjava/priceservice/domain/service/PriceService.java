package com.testjava.priceservice.domain.service;

import java.util.List;
import java.util.Optional;

import com.testjava.priceservice.domain.mapper.PriceDomainMapper;
import com.testjava.priceservice.domain.model.Price;
import com.testjava.priceservice.domain.model.PriceQuery;
import com.testjava.priceservice.domain.model.PriceResult;
import com.testjava.priceservice.domain.port.PriceRepository;
import com.testjava.priceservice.domain.port.PriceServicePort;
import com.testjava.priceservice.domain.strategy.PriceSelectionStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PriceService implements PriceServicePort {

  private final PriceRepository priceRepository;
  private final PriceDomainMapper domainMapper;
  private final PriceSelectionStrategy priceSelectionStrategy;

  @Override
  public Optional<PriceResult> findApplicablePrice(final PriceQuery query) {
    log.debug(
        "Finding applicable price for query: productId={}, brandId={}, applicationDate={}",
        query.getProductId(),
        query.getBrandId(),
        query.getApplicationDate());

    // Fetch all potentially applicable prices
    final List<Price> prices =
        priceRepository.findApplicablePrices(
            query.getApplicationDate(), query.getProductId(), query.getBrandId());

    log.debug("Repository returned {} candidate prices", prices.size());

    // Apply strategy to select the appropriate price
    final Optional<Price> selectedPrice = priceSelectionStrategy.selectPrice(prices);

    // Map selected price to result
    final Optional<PriceResult> result = selectedPrice.map(domainMapper::mapToResult);

    if (result.isPresent()) {
      PriceResult pr = result.get();
      log.debug(
          "Found applicable price using {}: priceList={}, price={}",
          priceSelectionStrategy.getStrategyName(),
          pr.getPriceList(),
          pr.getPrice());
    } else {
      log.debug("No applicable price found for the given criteria");
    }

    return result;
  }
}
