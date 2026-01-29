package com.testjava.priceservice.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties for application security. Maps properties starting with "app.security"
 * from application.yml.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppSecurityProperties {

  private final Security security = new Security();

  @Getter
  @Setter
  public static class Security {
    private final User user = new User();

    @Getter
    @Setter
    public static class User {
      private String username;
      private String password;
    }
  }
}
