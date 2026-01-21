package com.testjava.priceservice.common;

/**
 * Test categories for organizing test execution by layers
 * Enables running specific test types independently for faster feedback
 */
public final class TestCategories {

    private TestCategories() {
        // Utility class
    }

    /**
     * Fast unit tests that test individual components in isolation
     * Should run in < 100ms each and use mocks for dependencies
     */
    public interface UnitTest {
    }

    /**
     * API/Controller tests that test the REST endpoints
     * Uses MockMvc and focuses on HTTP layer validation
     */
    public interface ApiTest {
    }

    /**
     * Integration tests that test component interaction
     * May use real databases (H2 in-memory) and Spring context
     */
    public interface IntegrationTest {
    }

    /**
     * End-to-end tests that test complete user scenarios
     * Uses full application context and real data flows
     */
    public interface EndToEndTest {
    }

    /**
     * Performance tests for load testing and benchmarking
     * Should be run separately from regular test suite
     */
    public interface PerformanceTest {
    }
}