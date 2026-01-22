package com.testjava.priceservice.infrastructure.persistence.adapter;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.testjava.priceservice.domain.model.Price;
import com.testjava.priceservice.domain.port.PriceRepository;
import com.testjava.priceservice.infrastructure.persistence.entity.PriceEntity;
import com.testjava.priceservice.infrastructure.persistence.mapper.PriceEntityMapper;
import com.testjava.priceservice.infrastructure.persistence.repository.JpaPriceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepository {

  private final JpaPriceRepository jpaPriceRepository;
  private final PriceEntityMapper entityMapper;

  @Override
  public Optional<Price> findApplicablePrice(
      LocalDateTime applicationDate, Long productId, Long brandId) {
    log.debug(
        "Searching for applicable prices - date: {}, productId: {}, brandId: {}",
        applicationDate,
        productId,
        brandId);

    Optional<PriceEntity> entity =
        jpaPriceRepository.findApplicablePrice(applicationDate, productId, brandId);

    log.debug(
        "Price entity {}found for given criteria with highest priority",
        entity.isPresent() ? "" : "not ");

    return entity.map(entityMapper::mapToDomain);
  }
}
