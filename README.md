# kairos-microservicetest

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![Code Coverage](https://img.shields.io/badge/coverage-93%25-brightgreen.svg)]()
[![Tests](https://img.shields.io/badge/tests-108%2F108-brightgreen.svg)]()
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

A production-ready Spring Boot microservice implementing **Hexagonal Architecture** and **Domain-Driven Design** principles for e-commerce price management with time-based pricing rules and priority handling.

## 🎯 Overview

The service provides intelligent price resolution with temporal validity and priority rules, automatically selecting the most applicable price for products based on date, brand, and priority hierarchy.

#############################################################
#### Business Logic needs to be enhanced
There are a lot of countries with double currency in commerce. (European non-Euro countries as Poland, South-American countries)
We need to enhance the business logic to handle this case.
Options:
- Add currency parameter in the call.
- Return a list of prices/currencies values.

#############################################################


### Core Features

✅ **Smart Price Resolution** - Automatic highest-priority price selection
✅ **Temporal Validation** - Date-range based price applicability
✅ **Multi-Brand Support** - Brand-specific pricing management
✅ **RESTful API** - Clean, well-documented endpoints with OpenAPI/Swagger
✅ **Security** - Spring Security with HTTP Basic authentication
✅ **Monitoring** - Spring Boot Actuator for health checks and metrics
✅ **Code Quality** - Checkstyle, Spotless formatting, and 93% test coverage
✅ **Production Ready** - Kubernetes manifests, Docker support, CI/CD pipelines

## 🏗️ Architecture

### Clean Hexagonal Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Infrastructure Layer                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐  │
│  │     Web     │  │ Persistence │  │  Configuration  │  │
│  │ Controllers │  │  Adapters   │  │ Security/Config │  │
│  └─────────────┘  └─────────────┘  └─────────────────┘  │
└─────────────────────┬───────────────────┬───────────────┘
                      │                   │
┌─────────────────────▼───────────────────▼───────────────┐
│                  Application Layer                      │
│  ┌─────────────┐              ┌─────────────────────┐   │
│  │  Use Cases  │              │  Application Ports  │   │
│  │ Orchestration│              │   & Services        │   │
│  └─────────────┘              └─────────────────────┘   │
└─────────────────────┬───────────────────┬───────────────┘
                      │                   │
┌─────────────────────▼───────────────────▼───────────────┐
│                    Domain Layer                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐  │
│  │   Models    │  │  Services   │  │  Domain Ports   │  │
│  │ & Entities  │  │ & Logic     │  │  & Validators   │  │
│  └─────────────┘  └─────────────┘  └─────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

### Key Components

| Layer | Component | Responsibility |
|-------|-----------|---------------|
| **Domain** | `Price`, `PriceQuery`, `PriceResult` | Core business entities |
| **Domain** | `PriceService` | Business logic implementation |
| **Domain** | `PriceQueryValidator` | Domain validation rules |
| **Application** | `FindPriceUseCase` | Use case orchestration |
| **Infrastructure** | `PriceController` | REST API endpoints |
| **Infrastructure** | `PriceRepositoryAdapter` | Data persistence adapter |
| **Infrastructure** | `SecurityConfig` | Authentication and security headers |

## 🚀 Quick Start

### Prerequisites
- **Java 17+** ☕
- **Gradle 8.4+** 🐘
- **Docker** (optional) 🐳

### Run Locally

```bash
# Clone the repository
git clone <repository-url>
cd kairos-microservicetest

# Run the application
./gradlew bootRun

# The application will start on http://localhost:8080
# Default credentials: user/password
```

### Using Docker

```bash
# Build Docker image
docker build -t price-service:latest .

# Run container
docker run -p 8080:8080 \
  -e APP_SECURITY_USER=admin \
  -e APP_SECURITY_PASSWORD=secret \
  price-service:latest
```

## 📡 API Reference

### Get Applicable Price

```http
GET /api/prices?applicationDate={date}&productId={id}&brandId={id}
Authorization: Basic <credentials>
```

#### Parameters

| Parameter | Type | Format | Required | Description |
|-----------|------|--------|----------|-------------|
| `applicationDate` | string | `yyyy-MM-dd-HH:mm:ss` | Yes | Query date/time |
| `productId` | integer | int64 | Yes | Product identifier (must be positive) |
| `brandId` | integer | int64 | Yes | Brand identifier (must be positive) |

#### Response Examples

**✅ Success (200)**
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14-15.00.00",
  "endDate": "2020-06-14-18.30.00",
  "price": 25.45
}
```

**❌ Not Found (404)**
```json
{
  "timestamp": "2026-01-22T10:30:00Z",
  "status": 404,
  "error": "Price Not Found",
  "message": "No applicable price found for the given criteria",
  "details": {
    "productId": 35455,
    "brandId": 1,
    "applicationDate": "2026-01-22T10:30:00"
  }
}
```

**⚠️ Bad Request (400)** - Invalid Parameters
```json
{
  "timestamp": "2026-01-22T10:30:00Z",
  "status": 400,
  "error": "Validation Error",
  "message": "Product ID must be positive"
}
```

### Example Request

```bash
# Using curl with Basic Auth
curl -u user:password \
  "http://localhost:8080/api/prices?applicationDate=2020-06-14-16:00:00&productId=35455&brandId=1"
```

### Health & Monitoring

| Endpoint | Description | Auth Required |
|----------|-------------|---------------|
| `/actuator/health` | Application health status | No |
| `/actuator/info` | Application information | No |
| `/api-docs` | OpenAPI specification | No |
| `/swagger-ui.html` | Interactive API documentation | No |

## 🧪 Testing

### Test Strategy

The project implements comprehensive testing with **93% test coverage** and **108 passing tests**:

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "PriceControllerTest"

# Run tests by pattern
./gradlew test --tests "*IntegrationTest"

# Generate coverage report
./gradlew jacocoTestReport
# Report: build/reports/jacoco/test/html/index.html
```

### Test Coverage

- **Total Tests**: 108 tests (all passing)
- **Unit Tests**: Domain, Application, and Infrastructure layers
- **Integration Tests**: Database, Security, Validation
- **Test Coverage**: 93% line coverage
- **Test Categories**: Unit, Integration, Performance, Security

### Test Scenarios

1. **Test 1**: 2020-06-14 10:00 → Price 35.50€ (Priority 0)
2. **Test 2**: 2020-06-14 16:00 → Price 25.45€ (Priority 1) - Higher priority wins
3. **Test 3**: 2020-06-14 21:00 → Price 35.50€ (Priority 0)
4. **Test 4**: 2020-06-15 10:00 → Price 30.50€ (Priority 1)
5. **Test 5**: 2020-06-16 21:00 → Price 38.95€ (Priority 1)

## 🔧 Development

### Build Commands

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Check code style
./gradlew checkstyleMain checkstyleTest

# Format code (Google Java Format)
./gradlew spotlessApply

# Check code formatting
./gradlew spotlessCheck

# Clean build
./gradlew clean build
```

### Code Quality Tools

| Tool | Purpose | Configuration |
|------|---------|---------------|
| **Checkstyle** | Code style enforcement | `config/checkstyle/checkstyle.xml` |
| **Spotless** | Automated code formatting | Google Java Format 1.19.2 |
| **JaCoCo** | Code coverage analysis | 93% coverage achieved |

### Code Formatting

The project uses **Spotless** with **Google Java Format**:

```bash
# Apply formatting to all files
./gradlew spotlessApply

# Check if code is formatted correctly (CI/CD)
./gradlew spotlessCheck
```

**Formatting Rules**:
- Google Java Format style (2-space indentation)
- Import order: java → jakarta → javax → org → com
- Removes unused imports
- Ensures newline at end of file

## 🔐 Security

### Security Features

✅ **Authentication** - Spring Security with HTTP Basic Auth
✅ **Input Validation** - Bean Validation with custom constraints
✅ **Security Headers** - X-Frame-Options, X-Content-Type-Options, HSTS, XSS Protection
✅ **SQL Injection Prevention** - JPA parameterized queries
✅ **H2 Console** - Disabled by default in production
✅ **Environment Secrets** - Externalized configuration

### Authentication

The API requires Basic Authentication for price endpoints:

```bash
# Default credentials (development)
Username: user
Password: password

# Configure via environment variables
export APP_SECURITY_USER=admin
export APP_SECURITY_PASSWORD=strong-password
```

### Security Headers

Configured security headers:
- **X-Frame-Options**: DENY (prevents clickjacking)
- **X-Content-Type-Options**: nosniff (prevents MIME sniffing)
- **X-XSS-Protection**: Enabled
- **Strict-Transport-Security**: 1 year, includeSubDomains

## 🚀 Deployment

### Kubernetes

Kubernetes manifests available in `k8s/`:

```bash
# Apply all manifests
kubectl apply -f k8s/

# Or individually
kubectl apply -f k8s/secret.yaml      # Secrets (configure first!)
kubectl apply -f k8s/configmap.yaml   # Configuration
kubectl apply -f k8s/deployment.yaml  # Application deployment
kubectl apply -f k8s/service.yaml     # Service
kubectl apply -f k8s/hpa.yaml         # Horizontal Pod Autoscaler
```

**⚠️ Security Note**: Update `k8s/secret.yaml` with actual credentials before deployment. Never commit real secrets to version control.

### CI/CD Pipeline

GitHub Actions workflow (`.github/workflows/ci-cd.yml`):

1. ✅ Build with Gradle
2. ✅ Run all tests
3. ✅ Run Checkstyle
4. ✅ Generate JaCoCo coverage report
5. ✅ Upload build artifacts
6. ✅ Build Docker image

## 📊 Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SERVER_PORT` | 8080 | Application port |
| `TESTJAVA_DB_USERNAME` | sa | Database username |
| `TESTJAVA_DB_PASSWORD` | password | Database password |
| `APP_SECURITY_USER` | user | API username |
| `APP_SECURITY_PASSWORD` | password | API password |

### Application Profiles

- **default**: Development mode with H2 console disabled
- **test**: Fast test execution with minimal logging

## 🗄️ Database

### H2 In-Memory Database

**Configuration**:
- JDBC URL: `jdbc:h2:mem:testdb`
- Console: Disabled by default (enable with `spring.h2.console.enabled=true`)
- Schema: Auto-created on startup
- Data: Initialized from `data.sql`

**Sample Data**:
```sql
-- Product 35455 for Brand 1 (ZARA)
-- 4 price entries with different date ranges and priorities
```

## 📈 Performance

### Benchmarks

| Metric | Value |
|--------|-------|
| **Startup Time** | ~4-6 seconds |
| **Memory Usage** | ~256MB |
| **Response Time (P95)** | < 50ms |
| **Test Execution** | ~40s for all 108 tests |

## 🤝 Contributing

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Format** code (`./gradlew spotlessApply`)
4. **Test** changes (`./gradlew test`)
5. **Commit** with conventional commits (`git commit -m 'feat: add amazing feature'`)
6. **Push** to the branch (`git push origin feature/amazing-feature`)
7. **Open** a Pull Request

### Code Standards

- Follow **Hexagonal Architecture** principles
- Maintain **≥80% test coverage** (currently 93%)
- Use **Google Java Format** (via Spotless)
- Pass all **quality gates** (Checkstyle, tests)
- Write **conventional commits**

## 🛠️ Technology Stack

| Category | Technology |
|----------|-----------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.4.0 |
| **Security** | Spring Security 6.2 |
| **Database** | H2 (in-memory) |
| **Build Tool** | Gradle 8.12 |
| **Testing** | JUnit 5, Mockito, AssertJ |
| **Documentation** | OpenAPI 3, Swagger UI |
| **Code Quality** | Checkstyle, Spotless, JaCoCo |
| **Containerization** | Docker |
| **Orchestration** | Kubernetes |

## 📝 Known Limitations

- **Currency Support**: Single currency (EUR) - not handling multi-currency scenarios
- **Country Support**: No country-specific pricing
- **In-Memory Database**: H2 for development (use PostgreSQL/MySQL for production)
- **Authentication**: Basic Auth (consider OAuth2/JWT for production)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📚 Documentation

- **API Documentation**: Available at `/swagger-ui.html` when running
- **Changelog**: See [CHANGELOG.md](CHANGELOG.md) for version history
