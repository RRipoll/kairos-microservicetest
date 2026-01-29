package com.testjava.priceservice.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(AppSecurityProperties.class)
public class SecurityConfig {

  private final AppSecurityProperties appSecurityProperties;

  public SecurityConfig(AppSecurityProperties appSecurityProperties) {
    this.appSecurityProperties = appSecurityProperties;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // CSRF is disabled for stateless REST API
        // Re-enable if adding state-changing operations or form-based auth
        .csrf(AbstractHttpConfigurer::disable)

        // Add security headers
        .headers(
            headers ->
                headers
                    // Prevent clickjacking attacks
                    .frameOptions(frame -> frame.deny())
                    // Prevent MIME type sniffing
                    .contentTypeOptions(Customizer.withDefaults())
                    // Enable XSS protection (browser-level)
                    .xssProtection(Customizer.withDefaults())
                    // Enforce HTTPS in production (configure per environment)
                    .httpStrictTransportSecurity(
                        hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000)))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/api/prices/**")
                    .authenticated()
                    // SECURITY NOTE: Consider requiring authentication for these endpoints in
                    // production
                    .requestMatchers(
                        "/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    AppSecurityProperties.Security.User userConfig = appSecurityProperties.getSecurity().getUser();
    UserDetails user =
        User.builder()
            .username(userConfig.getUsername())
            .password(passwordEncoder.encode(userConfig.getPassword()))
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(user);
  }
}
