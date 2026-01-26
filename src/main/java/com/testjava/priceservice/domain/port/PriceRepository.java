package com.testjava.priceservice.domain.port;

import java.time.LocalDateTime;
import java.util.List;

import com.testjava.priceservice.domain.model.Price;

public interface PriceRepository {

  List<Price> findApplicablePrices(LocalDateTime applicationDate, Long productId, Long brandId);
}
