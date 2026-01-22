package com.testjava.priceservice.domain.service;

import java.util.Optional;

import com.testjava.priceservice.domain.mapper.PriceDomainMapper;
import com.testjava.priceservice.domain.model.PriceQuery;
import com.testjava.priceservice.domain.model.PriceResult;
import com.testjava.priceservice.domain.port.PriceRepository;
import com.testjava.priceservice.domain.port.PriceServicePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PriceService implements PriceServicePort {

  private final PriceRepository priceRepository;
  private final PriceDomainMapper domainMapper;

  @Override
  public Optional<PriceResult> findApplicablePrice(final PriceQuery query) {
    log.debug(
        "Finding applicable price for query: productId={}, brandId={}, applicationDate={}",
        query.getProductId(),
        query.getBrandId(),
        query.getApplicationDate());

    final Optional<PriceResult> result =
        priceRepository
            .findApplicablePrice(
                query.getApplicationDate(), query.getProductId(), query.getBrandId())
            .map(domainMapper::mapToResult);

    if (result.isPresent()) {
      PriceResult pr = result.get();
      log.debug("Found applicable price: priceList={}, price={}", pr.getPriceList(), pr.getPrice());
    } else {
      log.debug("No applicable price found for the given criteria");
    }

    return result;
  }
}
