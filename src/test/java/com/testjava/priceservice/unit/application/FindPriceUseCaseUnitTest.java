package com.testjava.priceservice.unit.application;

import com.testjava.priceservice.application.port.FindPriceUseCasePort;

import com.testjava.priceservice.application.usecase.FindPriceUseCase;
import com.testjava.priceservice.common.TestCategories;
import com.testjava.priceservice.common.TestDataFactory;
import com.testjava.priceservice.domain.model.PriceQuery;
import com.testjava.priceservice.domain.model.PriceResult;
import com.testjava.priceservice.domain.port.PriceServicePort;
import com.testjava.priceservice.domain.validator.PriceQueryValidator;
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
@DisplayName("Unit Tests - Find Price Use Case")
class FindPriceUseCaseUnitTest implements TestCategories.UnitTest {

    @Mock
    private PriceServicePort priceService;

    @Mock
    private PriceQueryValidator queryValidator;

    private FindPriceUseCasePort findPriceUseCase;

    @BeforeEach
    void setUp() {
        findPriceUseCase = new FindPriceUseCase(priceService, queryValidator);
    }

    @Test
    @DisplayName("Should execute successfully with valid query")
    void shouldExecuteSuccessfullyWithValidQuery() {
        // Given
        PriceQuery query = TestDataFactory.createTestQuery();
        PriceResult expectedResult = TestDataFactory.createTestPriceResult();

        when(priceService.findApplicablePrice(query))
                .thenReturn(Optional.of(expectedResult));

        // When
        Optional<PriceResult> result = findPriceUseCase.execute(query);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedResult);

        verify(queryValidator).validate(query);
        verify(priceService).findApplicablePrice(query);
    }

    @Test
    @DisplayName("Should return empty when no price found")
    void shouldReturnEmptyWhenNoPriceFound() {
        // Given
        PriceQuery query = TestDataFactory.createTestQuery();

        when(priceService.findApplicablePrice(query))
                .thenReturn(Optional.empty());

        // When
        Optional<PriceResult> result = findPriceUseCase.execute(query);

        // Then
        assertThat(result).isEmpty();

        verify(queryValidator).validate(query);
        verify(priceService).findApplicablePrice(query);
    }

    @Test
    @DisplayName("Should validate query before execution")
    void shouldValidateQueryBeforeExecution() {
        // Given
        PriceQuery query = TestDataFactory.createTestQuery();

        // When
        findPriceUseCase.execute(query);

        // Then
        verify(queryValidator).validate(query);
        verify(priceService).findApplicablePrice(query);
    }

}