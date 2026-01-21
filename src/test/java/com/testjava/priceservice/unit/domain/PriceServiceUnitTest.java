package com.testjava.priceservice.unit.domain;

import com.testjava.priceservice.common.TestCategories;
import com.testjava.priceservice.common.TestDataFactory;
import com.testjava.priceservice.domain.mapper.PriceDomainMapper;
import com.testjava.priceservice.domain.model.Price;
import com.testjava.priceservice.domain.model.PriceQuery;
import com.testjava.priceservice.domain.model.PriceResult;
import com.testjava.priceservice.domain.port.PriceRepository;
import com.testjava.priceservice.domain.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static com.testjava.priceservice.common.TestDataFactory.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag(UNIT_TAG)
@DisplayName("Unit Tests - Price Service")
class PriceServiceUnitTest implements TestCategories.UnitTest {

        @Mock
        private PriceRepository priceRepository;

        @Mock
        private PriceDomainMapper domainMapper;

        private PriceService priceService;

        @BeforeEach
        void setUp() {
                priceService = new PriceService(priceRepository, domainMapper);
        }

        @Test
        @DisplayName("Should return price result when highest priority price found")
        void shouldReturnPriceResultWhenHighestPriorityPriceFound() {
                // Given
                PriceQuery query = TestDataFactory.createTestQuery();
                Price highestPriorityPrice = TestDataFactory.createTestPrice(2, 1, TEST_PRICE_25_45);
                PriceResult expectedResult = TestDataFactory.createTestPriceResult();

                when(priceRepository.findApplicablePrice(
                                query.getApplicationDate(),
                                query.getProductId(),
                                query.getBrandId())).thenReturn(Optional.of(highestPriorityPrice));

                when(domainMapper.mapToResult(highestPriorityPrice))
                                .thenReturn(expectedResult);

                // When
                Optional<PriceResult> result = priceService.findApplicablePrice(query);

                // Then
                assertThat(result).contains(expectedResult);

                verify(priceRepository).findApplicablePrice(
                                query.getApplicationDate(),
                                query.getProductId(),
                                query.getBrandId());
                verify(domainMapper).mapToResult(highestPriorityPrice);
        }

        @Test
        @DisplayName("Should return empty when no prices found")
        void shouldReturnEmptyWhenNoPricesFound() {
                // Given
                PriceQuery query = TestDataFactory.createTestQuery();

                when(priceRepository.findApplicablePrice(
                                query.getApplicationDate(),
                                query.getProductId(),
                                query.getBrandId())).thenReturn(Optional.empty());

                // When
                Optional<PriceResult> result = priceService.findApplicablePrice(query);

                // Then
                assertThat(result).isEmpty();

                verify(priceRepository).findApplicablePrice(
                                query.getApplicationDate(),
                                query.getProductId(),
                                query.getBrandId());
                verifyNoInteractions(domainMapper);
        }

        @Test
        @DisplayName("Should return first price when database query returns single result")
        void shouldReturnFirstPriceWhenDatabaseQueryReturnsSingleResult() {
                // Given
                PriceQuery query = TestDataFactory.createTestQuery();
                Price price = TestDataFactory.createTestPrice();
                PriceResult expectedResult = TestDataFactory.createTestPriceResult();

                when(priceRepository.findApplicablePrice(
                                query.getApplicationDate(),
                                query.getProductId(),
                                query.getBrandId())).thenReturn(Optional.of(price));

                when(domainMapper.mapToResult(price)).thenReturn(expectedResult);

                // When
                Optional<PriceResult> result = priceService.findApplicablePrice(query);

                // Then
                assertThat(result).contains(expectedResult);

        }
}