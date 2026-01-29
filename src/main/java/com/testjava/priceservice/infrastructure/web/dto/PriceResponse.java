package com.testjava.priceservice.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.testjava.priceservice.infrastructure.common.DateFormats;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Price response containing the applicable price information for a product")
public class PriceResponse {

  @Schema(description = "Product identifier", example = "35455")
  private Long productId;

  @Schema(description = "Brand identifier", example = "1")
  private Long brandId;

  @Schema(description = "Price list identifier", example = "2")
  private Integer priceList;

  @JsonFormat(pattern = DateFormats.RESPONSE_DATE_TIME_FORMAT)
  @Schema(description = "Start date of price validity", example = "2020-06-14-15.00.00")
  private LocalDateTime startDate;

  @JsonFormat(pattern = DateFormats.RESPONSE_DATE_TIME_FORMAT)
  @Schema(description = "End date of price validity", example = "2020-06-14-18.30.00")
  private LocalDateTime endDate;

  @Schema(description = "Final sale price", example = "25.45")
  private BigDecimal price;

  @Schema(description = "Currency ISO code", example = "EUR")
  private String currency;
}
