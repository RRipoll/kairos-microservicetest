package com.testjava.priceservice.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response containing details about why the request failed")
public class ErrorResponse {

  @Schema(description = "Timestamp of the error", example = "2024-01-21T17:30:00")
  private LocalDateTime timestamp;

  @Schema(description = "HTTP status code", example = "404")
  private int status;

  @Schema(description = "Error category", example = "Not Found")
  private String error;

  @Schema(description = "Detailed error message", example = "No price found for product 35455")
  private String message;

  @Schema(description = "Additional context or metadata about the error")
  private Map<String, Object> details;
}
