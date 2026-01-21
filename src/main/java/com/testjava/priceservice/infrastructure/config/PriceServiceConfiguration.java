package com.testjava.priceservice.infrastructure.config;

import com.testjava.priceservice.domain.mapper.PriceDomainMapper;
import com.testjava.priceservice.domain.port.PriceRepository;
import com.testjava.priceservice.domain.port.PriceServicePort;
import com.testjava.priceservice.domain.service.PriceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriceServiceConfiguration {

    @Bean
    public PriceServicePort priceService(
            PriceRepository priceRepository,
            PriceDomainMapper domainMapper) {
        return new PriceService(priceRepository, domainMapper);
    }
}