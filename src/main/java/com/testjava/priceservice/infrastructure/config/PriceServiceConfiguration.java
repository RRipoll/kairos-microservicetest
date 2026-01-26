package com.testjava.priceservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.testjava.priceservice.domain.mapper.PriceDomainMapper;
import com.testjava.priceservice.domain.port.PriceRepository;
import com.testjava.priceservice.domain.port.PriceServicePort;
import com.testjava.priceservice.domain.service.PriceService;
import com.testjava.priceservice.domain.strategy.PriceSelectionStrategy;

@Configuration
public class PriceServiceConfiguration {

  @Bean
  public PriceServicePort priceService(
      PriceRepository priceRepository,
      PriceDomainMapper domainMapper,
      PriceSelectionStrategy priceSelectionStrategy) {
    return new PriceService(priceRepository, domainMapper, priceSelectionStrategy);
  }
}
