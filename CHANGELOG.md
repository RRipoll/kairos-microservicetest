# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] - 2026-01-28

Initial release of the Price Service microservice.

### Added

- **Spring Boot 3.4.0 microservice** with Java 17
- **Hexagonal Architecture** implementation with Domain-Driven Design principles
  - Domain Layer: Core business entities and logic
  - Application Layer: Use case orchestration
  - Infrastructure Layer: Web, persistence, and configuration adapters
- **REST API endpoint** for price queries: `GET /api/prices`
  - Query parameters: `applicationDate`, `productId`, `brandId`
  - Response includes: product details, price list, dates, and final price
  - HTTP status codes: 200 (OK), 400 (Bad Request), 404 (Not Found), 401 (Unauthorized)
- **Smart price resolution** with priority-based selection
  - Temporal validation based on date ranges
  - Automatic selection of highest priority price when multiple prices overlap
- **Spring Security** with HTTP Basic authentication
  - BCrypt password encoding
  - Configurable credentials via environment variables
  - Security headers: X-Frame-Options, X-Content-Type-Options, HSTS, XSS Protection
- **Input validation** using Bean Validation
  - Required parameter validation with `@NotNull`
  - Positive number validation with `@Positive`
  - Proper error responses with descriptive messages
- **H2 in-memory database** for development and testing
  - Auto-initialized schema and sample data
  - Pre-loaded test data for product 35455 (ZARA brand)
  - H2 console disabled by default for security
- **OpenAPI/Swagger documentation**
  - Interactive API documentation at `/swagger-ui.html`
  - OpenAPI specification at `/api-docs`
- **Spring Boot Actuator** for monitoring
  - Health endpoint: `/actuator/health`
  - Info endpoint: `/actuator/info`
- **Comprehensive test suite** with 108 tests
  - 93% code coverage
  - Unit tests for all layers (Domain, Application, Infrastructure)
  - Integration tests for database, security, and validation
  - All 5 mandatory business test scenarios validated
- **Code quality tools**
  - Spotless with Google Java Format 1.19.2
  - Checkstyle for code style enforcement
  - JaCoCo for coverage reporting
- **Kubernetes deployment manifests**
  - Deployment with resource limits
  - Service (ClusterIP)
  - ConfigMap for configuration
  - Secret template for credentials
  - HorizontalPodAutoscaler for scaling
- **Docker support**
  - Multi-stage Dockerfile with Eclipse Temurin 17 JRE Alpine
  - Non-root user for security
- **CI/CD pipeline** with GitHub Actions
  - Automated build and test
  - Checkstyle validation
  - JaCoCo coverage report generation
  - Build artifact uploads
  - Docker image build
- **Comprehensive documentation**
  - README.md with architecture, API reference, and deployment guide
  - CI-CD-README.md with pipeline documentation
  - Requirements_Specification_Analysis.md with business analysis

---

## [Unreleased]

### Planned Features

- OAuth2/JWT authentication for production use
- Multi-currency support with exchange rates
- Country-specific pricing capabilities
- PostgreSQL/MySQL database support for production
- API rate limiting and throttling
- Redis caching for performance optimization
- Distributed tracing with OpenTelemetry
- API versioning strategy

### Known Limitations

- No country-specific pricing
- H2 in-memory database (not suitable for production)
- Basic Authentication (consider OAuth2/JWT for production environments)
