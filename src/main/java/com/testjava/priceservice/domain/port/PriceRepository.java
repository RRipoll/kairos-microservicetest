package com.testjava.priceservice.domain.port;

import java.time.LocalDateTime;
import java.util.Optional;

import com.testjava.priceservice.domain.model.Price;

public interface PriceRepository {

  Optional<Price> findApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId);
}
