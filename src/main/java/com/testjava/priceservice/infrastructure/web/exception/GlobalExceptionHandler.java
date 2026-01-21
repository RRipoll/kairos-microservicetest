package com.testjava.priceservice.infrastructure.web.exception;

import com.testjava.priceservice.domain.exception.InvalidPriceQueryException;
import com.testjava.priceservice.domain.exception.PriceNotFoundException;
import com.testjava.priceservice.domain.exception.PriceServiceException;
import com.testjava.priceservice.infrastructure.common.ErrorMessages;
import com.testjava.priceservice.infrastructure.common.ResponseFields;
import com.testjava.priceservice.infrastructure.web.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler responsible for converting domain exceptions
 * to appropriate HTTP responses. This class has the single responsibility
 * of handling exception-to-HTTP-response mapping for the web layer.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles price not found exceptions.
     * Maps to HTTP 404 Not Found.
     */
    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePriceNotFoundException(PriceNotFoundException ex) {
        log.warn("Price not found: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put(ResponseFields.PRODUCT_ID, ex.getProductId());
        details.put(ResponseFields.BRAND_ID, ex.getBrandId());
        details.put(ResponseFields.APPLICATION_DATE, ex.getApplicationDate());

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(ErrorMessages.PRICE_NOT_FOUND)
                .message(ex.getMessage())
                .details(details)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Handles invalid price query exceptions.
     * Maps to HTTP 400 Bad Request.
     */
    @ExceptionHandler(InvalidPriceQueryException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPriceQueryException(InvalidPriceQueryException ex) {
        log.warn("Invalid price query: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put(ResponseFields.FIELD, ex.getField());
        details.put(ResponseFields.VALUE, ex.getValue());

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ErrorMessages.INVALID_REQUEST)
                .message(ex.getMessage())
                .details(details)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Handles illegal argument exceptions (validation errors).
     * Maps to HTTP 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Validation error: {}", ex.getMessage());

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ErrorMessages.VALIDATION_ERROR)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Handles general price service exceptions.
     * Maps to HTTP 422 Unprocessable Entity.
     */
    @ExceptionHandler(PriceServiceException.class)
    public ResponseEntity<ErrorResponse> handlePriceServiceException(PriceServiceException ex) {
        log.error("Price service error: {}", ex.getMessage(), ex);

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error(ErrorMessages.BUSINESS_LOGIC_ERROR)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    /**
     * Handles missing request parameters.
     * Maps to HTTP 400 Bad Request.
     */
    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
            org.springframework.web.bind.MissingServletRequestParameterException ex) {
        log.warn("Missing request parameter: {}", ex.getParameterName());

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Missing request parameter")
                .message("Required parameter '" + ex.getParameterName() + "' is missing")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Handles method argument type conversion errors.
     * Maps to HTTP 400 Bad Request.
     */
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        log.warn("Method argument type mismatch: {}", ex.getMessage());

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid parameter type")
                .message("Parameter '" + ex.getName() + "' has invalid value: " + ex.getValue())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Handles unexpected exceptions.
     * Maps to HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(ErrorMessages.INTERNAL_SERVER_ERROR)
                .message(ErrorMessages.UNEXPECTED_ERROR_OCCURRED)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}