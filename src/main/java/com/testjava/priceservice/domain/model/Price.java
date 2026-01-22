package com.testjava.priceservice.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Price {

  private final Long brandId;
  private final LocalDateTime startDate;
  private final LocalDateTime endDate;
  private final Integer priceList;
  private final Long productId;
  private final Integer priority;
  private final BigDecimal priceValue;
  private final String currency;

  public BigDecimal getPrice() {
    return priceValue;
  }

  public boolean isApplicableAt(final LocalDateTime applicationDate) {
    return !applicationDate.isBefore(startDate) && !applicationDate.isAfter(endDate);
  }
}
