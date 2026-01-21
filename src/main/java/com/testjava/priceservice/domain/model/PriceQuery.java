package com.testjava.priceservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PriceQuery {
    
    private final LocalDateTime applicationDate;
    private final Long productId;
    private final Long brandId;
}