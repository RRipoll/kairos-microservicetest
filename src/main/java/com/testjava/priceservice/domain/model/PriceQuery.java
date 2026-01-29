package com.testjava.priceservice.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PriceQuery {

  private final LocalDateTime applicationDate;
  private final Long productId;
  private final Long brandId;
}
