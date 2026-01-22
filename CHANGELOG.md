# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.2.0] - 2026-01-22

### Added - Security & Code Quality

#### Security Features
- **Spring Security Integration** - HTTP Basic authentication for API endpoints
- **Security Headers** - Comprehensive security headers configuration
  - X-Frame-Options: DENY (clickjacking prevention)
  - X-Content-Type-Options: nosniff (MIME sniffing prevention)
  - X-XSS-Protection (browser-level XSS protection)
  - Strict-Transport-Security with includeSubDomains
- **Input Validation** - Bean Validation with custom constraints
  - `@NotNull` validation on all required parameters
  - `@Positive` validation on productId and brandId
  - Custom validation messages
- **H2 Console Security** - Disabled by default in production
- **Kubernetes Secrets** - Enhanced security warnings and placeholder values
- **Security Integration Tests** - Comprehensive security test suite

#### Code Quality & Formatting
- **Spotless Code Formatter** - Automated code formatting with Google Java Format 1.19.2
  - Import ordering: java → jakarta → javax → org → com
  - Removes unused imports automatically
  - Ensures newline at end of file
  - Gradle build file formatting with Greclipse
- **Code Formatting Commands**
  - `./gradlew spotlessApply` - Apply formatting
  - `./gradlew spotlessCheck` - Verify formatting (CI/CD ready)

#### Resilience & Monitoring
- **Circuit Breaker** - Resilience4j integration
  - Sliding window: 10 calls
  - Failure threshold: 50%
  - Wait duration in open state: 5 seconds
  - Fallback returns 503 Service Unavailable
  - Health indicator integration
- **Actuator Endpoints** - Spring Boot Actuator configuration
  - `/actuator/health` - Application health with circuit breaker state
  - `/actuator/info` - Application information
  - Restricted endpoint exposure (health, info only by default)

#### Deployment & DevOps
- **Kubernetes Manifests** - Complete K8s deployment configuration
  - `deployment.yaml` - Application deployment with resource limits
  - `service.yaml` - ClusterIP service
  - `configmap.yaml` - Non-sensitive configuration
  - `secret.yaml` - Secrets template with security warnings
  - `hpa.yaml` - Horizontal Pod Autoscaler
- **CI/CD Pipeline** - GitHub Actions workflow
  - Automated builds on push/PR to main
  - Checkstyle code quality checks
  - JaCoCo coverage report generation
  - Artifact uploads (JAR, coverage reports)
  - Docker image builds
- **CLAUDE.md** - Comprehensive architecture and development guide

### Changed

#### Framework Updates
- **Spring Boot** - Updated from 3.2.0 to 3.4.0
- **Spring Security** - Updated to 6.2 with new configuration API
- **Gradle** - Updated to 8.12

#### Configuration Changes
- **Actuator Endpoints** - Reduced exposed endpoints from 4 to 2 (health, info only)
- **H2 Console** - Disabled by default (was enabled)
- **Application Port** - Changed default from 8082 to 8080
- **Test Configuration** - Optimized for faster execution with minimal logging

#### Security Improvements
- **Authentication Required** - All `/api/prices/**` endpoints now require authentication
- **Public Endpoints** - Swagger UI, actuator health, and API docs remain public
- **CSRF Protection** - Explicitly disabled for stateless REST API with documentation
- **Password Encoding** - BCrypt password encoding for user credentials

#### Testing Enhancements
- **Test Count** - Increased from 45+ to 108 comprehensive tests
- **Security Tests** - New security integration test suite
  - Authentication tests (with/without auth)
  - Authorization tests for public endpoints
  - Actuator endpoint access tests
- **Circuit Breaker Tests** - Integration tests for resilience patterns
  - Circuit state transitions (CLOSED → OPEN → HALF_OPEN → CLOSED)
  - Fallback behavior validation
  - Health indicator tests
- **Test Coverage** - Maintained 93% code coverage

### Fixed

#### Security Vulnerabilities
- **CRITICAL** - Removed plaintext credentials from Kubernetes secrets
- **CRITICAL** - Disabled H2 console by default for production safety
- **HIGH** - Added missing security headers
- **MEDIUM** - Implemented input validation with Bean Validation
- **MEDIUM** - Masked sensitive information in error responses

#### Test Fixes
- **Security Integration Tests** - Fixed database connection issues
  - Added `@ActiveProfiles("test")` annotation
  - Added `@Sql` annotations for test data loading
  - Updated `application-test.yml` to enable actuator endpoints for tests
- **Test Stability** - All 108 tests now passing consistently

### Security

#### Addressed Vulnerabilities
1. **Hardcoded Credentials** - K8s secrets now use placeholder values with clear warnings
2. **H2 Console Exposure** - Console disabled by default, only enabled explicitly in dev
3. **Missing Security Headers** - Added X-Frame-Options, HSTS, XSS Protection, Content-Type-Options
4. **Missing Input Validation** - Added Bean Validation annotations on all controller parameters
5. **Actuator Exposure** - Reduced exposed endpoints and added security notes

#### Security Best Practices
- Environment variable-based configuration for sensitive values
- BCrypt password encoding
- JPA parameterized queries (SQL injection prevention)
- Proper error handling without information leakage
- Security-focused documentation and warnings

### Documentation

#### New Documentation
- **CLAUDE.md** - Complete architectural guide and development reference
  - Hexagonal architecture explanation
  - Build and test commands
  - Security configuration details
  - Resilience patterns
  - Code formatting guidelines
- **Enhanced README** - Comprehensive project documentation
  - All current features and capabilities
  - Security features and configuration
  - Deployment instructions (Kubernetes, Docker)
  - Code quality tools and standards
  - Performance benchmarks

#### Updated Documentation
- **API Documentation** - Enhanced OpenAPI/Swagger annotations
  - Detailed parameter descriptions
  - Response examples for all status codes
  - Security requirements documentation
- **Configuration Guide** - Environment variables and profiles
- **Contribution Guide** - Code standards and formatting requirements

---

## [0.1.0] - 2025-09-08

### Added

#### Core Features
- Initial SpringBoot 3.2.0 application with Java 17
- Hexagonal architecture implementation with DDD principles
- REST API endpoint for price queries (`GET /api/prices`)
- H2 in-memory database with sample data
- Comprehensive test suite (45+ tests) covering all layers
- Swagger/OpenAPI 3 interactive documentation
- Database configuration with environment variable support
- Gradle build system with proper dependency management

#### Architecture
- **Domain Layer** - Core business logic (Price, PriceQuery, PriceResult)
- **Application Layer** - Use case orchestration (FindPriceUseCase)
- **Infrastructure Layer** - Adapters for web, persistence, and configuration
- **Ports & Adapters Pattern** - Clean separation of concerns
- **Dependency Inversion** - Domain-centric design

#### Testing
- Enhanced test architecture with comprehensive test organization
- Performance testing framework with concurrent load testing
- Improved test coverage reporting and analysis
- Quality gates and build verification processes
- Test categorization system for better organization
- Database integration tests for data layer validation

### API Endpoints

**Price Query API**
```
GET /api/prices?applicationDate={date}&productId={id}&brandId={id}
```

**Parameters**:
- `applicationDate` - Format: yyyy-MM-dd-HH:mm:ss
- `productId` - Numeric product identifier
- `brandId` - Numeric brand identifier

### Documentation

- Interactive Swagger UI at `/swagger-ui.html`
- OpenAPI specification at `/api-docs`
- H2 console at `/h2-console` (development only)

### Configuration

**Environment Variables**:
- `TESTJAVA_DB_USERNAME` - Database username (default: sa)
- `TESTJAVA_DB_PASSWORD` - Database password (default: password)
- `SERVER_PORT` - Application port (default: 8082)

### Technical Details

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: H2 in-memory
- **Build Tool**: Gradle 8.4
- **Testing**: JUnit 5 with Mockito
- **Documentation**: Swagger/OpenAPI 3
- **Architecture**: Hexagonal (Ports & Adapters)

### Sample Test Scenarios

1. **Test 1**: 2020-06-14 10:00 → Price 35.50€ (Priority 0)
2. **Test 2**: 2020-06-14 16:00 → Price 25.45€ (Priority 1)
3. **Test 3**: 2020-06-14 21:00 → Price 35.50€ (Priority 0)
4. **Test 4**: 2020-06-15 10:00 → Price 30.50€ (Priority 1)
5. **Test 5**: 2020-06-16 21:00 → Price 38.95€ (Priority 1)

### Test Coverage

- **Unit Tests**: 90 tests covering all business logic layers
- **Integration Tests**: 7 focused database integration tests
- **Performance Tests**: Load testing with concurrent request validation
- **Test Categories**: Organized tests by type (unit, integration, performance)
- **Coverage Target**: Maintained 100% test success rate

---

## [Unreleased]

### Planned Features

- **OAuth2/JWT Authentication** - Replace Basic Auth for production
- **Multi-Currency Support** - Handle different currencies and exchange rates
- **Country-Specific Pricing** - Support for geographic price variations
- **PostgreSQL/MySQL Support** - Production-ready database backends
- **Rate Limiting** - API throttling and rate limiting
- **Caching** - Redis integration for performance optimization
- **Distributed Tracing** - OpenTelemetry integration
- **API Versioning** - Support for multiple API versions

### Known Limitations

- Single currency support (EUR only)
- No country-specific pricing
- In-memory H2 database (not for production)
- Basic Auth (consider OAuth2/JWT for production)

---

## Version History

- **[0.2.0]** - Security, Code Quality & Production Readiness (2026-01-22)
- **[0.1.0]** - Initial Release with Hexagonal Architecture (2025-09-08)
