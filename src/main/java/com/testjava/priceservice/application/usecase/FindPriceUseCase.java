package com.testjava.priceservice.application.usecase;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.testjava.priceservice.application.port.FindPriceUseCasePort;
import com.testjava.priceservice.domain.model.PriceQuery;
import com.testjava.priceservice.domain.model.PriceResult;
import com.testjava.priceservice.domain.port.PriceServicePort;
import com.testjava.priceservice.domain.validator.PriceQueryValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindPriceUseCase implements FindPriceUseCasePort {

  private final PriceServicePort priceService;
  private final PriceQueryValidator queryValidator;

  @Override
  public Optional<PriceResult> execute(final PriceQuery query) {
    // Log query start
    if (query != null) {
      log.info(
          "Executing price lookup for productId={}, brandId={}, date={}",
          query.getProductId(),
          query.getBrandId(),
          query.getApplicationDate());
    }

    // Validate domain query
    queryValidator.validate(query);

    // Execute domain service
    final Optional<PriceResult> result = priceService.findApplicablePrice(query);

    // Log result
    if (result.isPresent()) {
      PriceResult pr = result.get();
      log.info(
          "Price lookup successful: found price={} for priceList={}",
          pr.getPrice(),
          pr.getPriceList());
    } else if (query != null) {
      log.warn(
          "Price lookup failed: no applicable price found for productId={}, brandId={}",
          query.getProductId(),
          query.getBrandId());
    }

    return result;
  }
}
