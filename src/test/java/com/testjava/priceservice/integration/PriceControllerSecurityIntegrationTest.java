package com.testjava.priceservice.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PriceControllerSecurityIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void whenAccessingPricesWithoutAuthThenUnauthorized() throws Exception {
    mockMvc
        .perform(
            get("/api/prices")
                .param("applicationDate", "2020-06-14-16:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(
      username = "user",
      roles = {"USER"})
  @Sql(scripts = {"/schema.sql", "/data.sql"})
  void whenAccessingPricesWithAuthThenOk() throws Exception {
    mockMvc
        .perform(
            get("/api/prices")
                .param("applicationDate", "2020-06-14-16:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isOk());
  }

  @Test
  void whenAccessingSwaggerThenNotUnauthorized() throws Exception {
    // Swagger might not be fully loaded in MockMvc context depending on
    // configuration
    // The important part is that it doesn't return 401 or 403
    mockMvc
        .perform(get("/v3/api-docs"))
        .andExpect(
            result -> {
              int status = result.getResponse().getStatus();
              if (status == 401 || status == 403) {
                throw new AssertionError("Expected not to be unauthorized, but got " + status);
              }
            });
  }

  @Test
  void whenAccessingActuatorThenOk() throws Exception {
    mockMvc.perform(get("/actuator/health")).andExpect(status().isOk());
  }
}
