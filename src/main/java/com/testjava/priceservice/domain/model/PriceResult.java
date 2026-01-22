package com.testjava.priceservice.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PriceResult {

  private final Long productId;
  private final Long brandId;
  private final Integer priceList;
  private final LocalDateTime startDate;
  private final LocalDateTime endDate;
  private final BigDecimal price;
}
