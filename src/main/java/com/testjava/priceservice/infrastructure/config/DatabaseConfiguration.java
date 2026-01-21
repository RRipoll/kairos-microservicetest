package com.testjava.priceservice.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Slf4j
@Configuration
public class DatabaseConfiguration {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @PostConstruct
    public void logDatabaseCredentials() {
        boolean usingDefaults = "sa".equals(username) && "password".equals(password);
        
        if (usingDefaults) {
            log.info("Database credentials: Using default credentials.");
            log.warn("Consider setting TESTJAVA_DB_USERNAME and TESTJAVA_DB_PASSWORD environment variables for production");
        } else {
            log.info("Database credentials: Using environment variables.");
        }
    }
}