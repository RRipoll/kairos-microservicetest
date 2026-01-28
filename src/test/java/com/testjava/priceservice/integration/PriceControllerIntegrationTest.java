package com.testjava.priceservice.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "spring.sql.init.mode=always")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "user", password = "password")
class PriceControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  // Test 1: Petición a las 10:00 del día 14 del producto 35455 for brand 1
  @Test
  @DisplayName("Test 1: Request at 10:00 on June 14th - Should return Price List 1 (35.50)")
  void test1_RequestAt1000_ShouldReturnPriceList1() throws Exception {
    mockMvc
        .perform(
            get("/api/prices")
                .param("applicationDate", "2020-06-14-10:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId").value(35455))
        .andExpect(jsonPath("$.brandId").value(1))
        .andExpect(jsonPath("$.priceList").value(1))
        .andExpect(jsonPath("$.price").value(35.50))
        .andExpect(jsonPath("$.currency").value("EUR"));
  }

  // Test 2: Petición a las 16:00 del día 14 del producto 35455 for brand 1
  @Test
  @DisplayName("Test 2: Request at 16:00 on June 14th - Should return Price List 2 (25.45)")
  void test2_RequestAt1600_ShouldReturnPriceList2() throws Exception {
    mockMvc
        .perform(
            get("/api/prices")
                .param("applicationDate", "2020-06-14-16:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId").value(35455))
        .andExpect(jsonPath("$.brandId").value(1))
        .andExpect(jsonPath("$.priceList").value(2))
        .andExpect(jsonPath("$.price").value(25.45))
        .andExpect(jsonPath("$.currency").value("EUR"));
  }

  // Test 3: Petición a las 21:00 del día 14 del producto 35455 for brand 1
  @Test
  @DisplayName("Test 3: Request at 21:00 on June 14th - Should return Price List 1 (35.50)")
  void test3_RequestAt2100_ShouldReturnPriceList1() throws Exception {
    mockMvc
        .perform(
            get("/api/prices")
                .param("applicationDate", "2020-06-14-21:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId").value(35455))
        .andExpect(jsonPath("$.brandId").value(1))
        .andExpect(jsonPath("$.priceList").value(1))
        .andExpect(jsonPath("$.price").value(35.50))
        .andExpect(jsonPath("$.currency").value("EUR"));
  }

  // Test 4: Petición a las 10:00 del día 15 del producto 35455 for brand 1
  @Test
  @DisplayName("Test 4: Request at 10:00 on June 15th - Should return Price List 3 (30.50)")
  void test4_RequestAt1000_inJune15_ShouldReturnPriceList3() throws Exception {
    mockMvc
        .perform(
            get("/api/prices")
                .param("applicationDate", "2020-06-15-10:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId").value(35455))
        .andExpect(jsonPath("$.brandId").value(1))
        .andExpect(jsonPath("$.priceList").value(3))
        .andExpect(jsonPath("$.price").value(30.50))
        .andExpect(jsonPath("$.currency").value("EUR"));
  }

  // Test 5: Petición a las 21:00 del día 16 del producto 35455 for brand 1
  @Test
  @DisplayName("Test 5: Request at 21:00 on June 16th - Should return Price List 4 (38.95)")
  void test5_RequestAt2100_inJune16_ShouldReturnPriceList4() throws Exception {
    mockMvc
        .perform(
            get("/api/prices")
                .param("applicationDate", "2020-06-16-21:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId").value(35455))
        .andExpect(jsonPath("$.brandId").value(1))
        .andExpect(jsonPath("$.priceList").value(4))
        .andExpect(jsonPath("$.price").value(38.95))
        .andExpect(jsonPath("$.currency").value("EUR"));
  }

  @Test
  @DisplayName("Should return 404 with error details when price is not found")
  void shouldReturn404_WhenPriceNotFound() throws Exception {
    mockMvc
        .perform(
            get("/api/prices")
                .param("applicationDate", "2025-01-01-00:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").exists())
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.details.productId").value(35455))
        .andExpect(jsonPath("$.details.brandId").value(1));
  }

  @Test
  @DisplayName("Should return 400 when productId is negative")
  void shouldReturn400_WhenProductIdIsNegative() throws Exception {
    mockMvc
        .perform(
            get("/api/prices")
                .param("applicationDate", "2020-06-14-10:00:00")
                .param("productId", "-1")
                .param("brandId", "1"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").exists())
        .andExpect(jsonPath("$.message").value("Product ID must be positive"));
  }

  @Test
  @DisplayName("Should return 400 when brandId is negative")
  void shouldReturn400_WhenBrandIdIsNegative() throws Exception {
    mockMvc
        .perform(
            get("/api/prices")
                .param("applicationDate", "2020-06-14-10:00:00")
                .param("productId", "35455")
                .param("brandId", "-1"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").exists())
        .andExpect(jsonPath("$.message").value("Brand ID must be positive"));
  }
}
