package com.testjava.priceservice.infrastructure.web.exception;

import com.testjava.priceservice.domain.exception.InvalidPriceQueryException;
import com.testjava.priceservice.domain.exception.PriceNotFoundException;
import com.testjava.priceservice.domain.exception.PriceServiceException;
import com.testjava.priceservice.infrastructure.common.ErrorMessages;
import com.testjava.priceservice.infrastructure.common.ResponseFields;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
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
    public ResponseEntity<Map<String, Object>> handlePriceNotFoundException(PriceNotFoundException ex) {
        log.warn("Price not found: {}", ex.getMessage());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ResponseFields.TIMESTAMP, LocalDateTime.now());
        body.put(ResponseFields.STATUS, HttpStatus.NOT_FOUND.value());
        body.put(ResponseFields.ERROR, ErrorMessages.PRICE_NOT_FOUND);
        body.put(ResponseFields.MESSAGE, ex.getMessage());
        body.put(ResponseFields.PRODUCT_ID, ex.getProductId());
        body.put(ResponseFields.BRAND_ID, ex.getBrandId());
        body.put(ResponseFields.APPLICATION_DATE, ex.getApplicationDate());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Handles invalid price query exceptions.
     * Maps to HTTP 400 Bad Request.
     */
    @ExceptionHandler(InvalidPriceQueryException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPriceQueryException(InvalidPriceQueryException ex) {
        log.warn("Invalid price query: {}", ex.getMessage());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ResponseFields.TIMESTAMP, LocalDateTime.now());
        body.put(ResponseFields.STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ResponseFields.ERROR, ErrorMessages.INVALID_REQUEST);
        body.put(ResponseFields.MESSAGE, ex.getMessage());
        body.put(ResponseFields.FIELD, ex.getField());
        body.put(ResponseFields.VALUE, ex.getValue());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Handles illegal argument exceptions (validation errors).
     * Maps to HTTP 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Validation error: {}", ex.getMessage());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ResponseFields.TIMESTAMP, LocalDateTime.now());
        body.put(ResponseFields.STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ResponseFields.ERROR, ErrorMessages.VALIDATION_ERROR);
        body.put(ResponseFields.MESSAGE, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Handles general price service exceptions.
     * Maps to HTTP 422 Unprocessable Entity.
     */
    @ExceptionHandler(PriceServiceException.class)
    public ResponseEntity<Map<String, Object>> handlePriceServiceException(PriceServiceException ex) {
        log.error("Price service error: {}", ex.getMessage(), ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ResponseFields.TIMESTAMP, LocalDateTime.now());
        body.put(ResponseFields.STATUS, HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put(ResponseFields.ERROR, ErrorMessages.BUSINESS_LOGIC_ERROR);
        body.put(ResponseFields.MESSAGE, ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    /**
     * Handles missing request parameters.
     * Maps to HTTP 400 Bad Request.
     */
    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingServletRequestParameter(
            org.springframework.web.bind.MissingServletRequestParameterException ex) {
        log.warn("Missing request parameter: {}", ex.getParameterName());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ResponseFields.TIMESTAMP, LocalDateTime.now());
        body.put(ResponseFields.STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ResponseFields.ERROR, "Missing request parameter");
        body.put(ResponseFields.MESSAGE, "Required parameter '" + ex.getParameterName() + "' is missing");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Handles method argument type conversion errors.
     * Maps to HTTP 400 Bad Request.
     */
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        log.warn("Method argument type mismatch: {}", ex.getMessage());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ResponseFields.TIMESTAMP, LocalDateTime.now());
        body.put(ResponseFields.STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ResponseFields.ERROR, "Invalid parameter type");
        body.put(ResponseFields.MESSAGE, "Parameter '" + ex.getName() + "' has invalid value: " + ex.getValue());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Handles unexpected exceptions.
     * Maps to HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ResponseFields.TIMESTAMP, LocalDateTime.now());
        body.put(ResponseFields.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put(ResponseFields.ERROR, ErrorMessages.INTERNAL_SERVER_ERROR);
        body.put(ResponseFields.MESSAGE, ErrorMessages.UNEXPECTED_ERROR_OCCURRED);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}