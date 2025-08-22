# Student Management API 🎓

A production-ready RESTful API for managing student records, built with **Java 21**, **Spring Boot 3.5.5**, and **PostgreSQL**.

## 🚀 Features

- **Complete CRUD Operations** - Create, read, update, and delete students
- **Multiple Access Patterns** - Find students by ID, email, or roll number
- **Auto-generated Roll Numbers** - Thread-safe sequence-based generation starting at 1000
- **Email Uniqueness Validation** - Prevents duplicate student emails
- **Comprehensive Error Handling** - Detailed error responses with field-level validation
- **API Versioning** - `/api/v1/` for future compatibility
- **Health Check Endpoints** - Database connectivity monitoring
- **Interactive API Documentation** - Swagger UI for testing and exploration
- **Production-Ready Configuration** - Environment variable driven settings

## 🛠️ Tech Stack

- **Java 21** - Latest LTS with modern language features
- **Spring Boot 3.5.5** - Enterprise-grade framework
- **Spring Data JPA** - Database abstraction layer
- **PostgreSQL** - Robust relational database
- **Flyway** - Database migration management
- **Testcontainers** - Integration testing with real database
- **Lombok** - Reduced boilerplate code
- **OpenAPI 3.0** - API documentation and client generation
- **Maven** - Dependency management and build automation

## 📋 Quick Start

### Prerequisites
- **Java 21+** (JDK)
- **PostgreSQL 12+** running locally
- **Maven 3.8+**
- **Git**

### 1. Clone and Setup
```bash
git clone <repository-url>
cd student-management-api
make install
```

### 2. Database Setup
```bash
# Create database
createdb student_db

# Run migrations
make db-migrate
```

### 3. Configure Environment
Create `src/main/resources/application-dev.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/student_db
    username: your_username
    password: your_password
```

### 4. Run Application
```bash
make run
```

### 5. Explore API
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/healthcheck
- **API Docs**: http://localhost:8080/api-docs

## 🔧 Development Workflow

### Essential Commands
```bash
# Setup project
make install

# Development cycle
make dev              # Clean → Build → Test → Run

# Individual steps
make build            # Compile application
make test             # Run all tests
make run              # Start in development mode
make run-prod         # Start in production mode

# Database operations
make db-migrate       # Apply database migrations
make db-status        # Check migration status

# Documentation
make docs             # Open Swagger UI
make swagger-export   # Export OpenAPI spec
make postman-generate # Generate Postman collection

# Monitoring
make health-check     # Quick health verification
make logs             # View application logs

# See all commands
make help
```

### Testing
```bash
# Run all tests
make test

# Unit tests only
make test-unit

# Integration tests only  
make test-integration
```

## 📊 Database Schema

The application uses PostgreSQL with Flyway migrations located in `src/main/resources/db/migration/`.

### Student Table
```sql
CREATE TABLE students (
    student_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    roll_number INTEGER NOT NULL UNIQUE DEFAULT nextval('student_roll_seq'),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Key Features:**
- **UUID Primary Keys** - Distributed system friendly
- **Auto-incrementing Roll Numbers** - Starting at 1000
- **Email Uniqueness** - Enforced at database level
- **Audit Timestamps** - Automatic creation and update tracking

## 🌐 API Endpoints

### Base URL: `/api/v1/students`

| Method | Endpoint | Description | Status Codes |
|--------|----------|-------------|--------------|
| `POST` | `/` | Create new student | 201, 400, 409 |
| `GET` | `/` | Get all students | 200 |
| `GET` | `/{id}` | Get student by ID | 200, 404 |
| `GET` | `/email/{email}` | Get student by email | 200, 404 |
| `GET` | `/rollnumber/{rollNumber}` | Get student by roll number | 200, 404 |
| `PUT` | `/{id}` | Update student | 200, 400, 404, 409 |
| `DELETE` | `/{id}` | Delete student by ID | 204 |
| `DELETE` | `/rollnumber/{rollNumber}` | Delete by roll number | 204 |

### Health Check Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/healthcheck` | Comprehensive health status |
| `GET` | `/healthcheck/simple` | Basic OK response |
| `GET` | `/healthcheck/database` | Database connectivity check |

## 📝 Request/Response Examples

### Create Student
```bash
POST /api/v1/students
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "dateOfBirth": "1995-06-15"
}
```

**Response (201 Created):**
```json
{
  "studentId": "123e4567-e89b-12d3-a456-426614174000",
  "rollNumber": 1001,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "dateOfBirth": "1995-06-15",
  "createdAt": "2025-08-23T10:30:00",
  "updatedAt": "2025-08-23T10:30:00"
}
```

### Error Response
```json
{
  "timestamp": "2025-08-23T10:30:00",
  "status": 409,
  "error": "EMAIL_ALREADY_EXISTS",
  "message": "Email john.doe@example.com already exists",
  "path": "/api/v1/students"
}
```

### Validation Error
```json
{
  "timestamp": "2025-08-23T10:30:00",
  "status": 400,
  "error": "VALIDATION_FAILED",
  "message": "Invalid input data",
  "path": "/api/v1/students",
  "fieldErrors": {
    "email": "must be a well-formed email address",
    "firstName": "must not be blank"
  }
}
```

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/student_db` |
| `DATABASE_USERNAME` | Database username | `student_user` |
| `DATABASE_PASSWORD` | Database password | `student_password` |
| `SERVER_PORT` | Application port | `8080` |
| `LOG_LEVEL` | Logging level | `INFO` |
| `SHOW_SQL` | Show SQL queries | `false` |

### Profiles

- **default** - Base configuration
- **dev** - Development mode (debug logging, SQL visibility)
- **prod** - Production mode (optimized settings)

### Running with Different Profiles
```bash
# Development
make run-profile PROFILE=dev

# Production  
make run-prod

# Custom profile
java -jar target/student-management-api-1.0.0.jar --spring.profiles.active=dev
```

## 📱 Postman Collection

### Generate Collection
```bash
# Start application
make run

# Export OpenAPI spec and generate Postman collection
make postman-generate
```

**Generated files:**
- `docs/api-spec.json` - OpenAPI 3.0 specification
- `docs/student-management-api.postman_collection.json` - Postman collection

### Import to Postman
1. Open Postman
2. Click **Import**
3. Select the generated collection file
4. Configure environment variables if needed

## 🧪 Testing

### Test Coverage
- **Unit Tests** - Service layer with Mockito
- **Repository Tests** - With Testcontainers and PostgreSQL
- **Validation Tests** - Request/response validation
- **Exception Handling Tests** - Error scenarios

### Running Tests
```bash
# All tests
make test

# Unit tests only
make test-unit

# Integration tests only
make test-integration

# With coverage report
mvn test jacoco:report
```

### Test Database
Tests use **Testcontainers** with PostgreSQL for production parity. No manual database setup required for testing.

## 🚀 Production Deployment

### Build for Production
```bash
make release
```

This creates:
- `target/student-management-api-1.0.0.jar` - Executable JAR
- `docs/api-spec.json` - API specification
- `docs/student-management-api.postman_collection.json` - Postman collection

### Environment Variables for Production
```bash
export DATABASE_URL=jdbc:postgresql://prod-server:5432/student_db
export DATABASE_USERNAME=prod_user
export DATABASE_PASSWORD=secure_password_123
export SPRING_PROFILES_ACTIVE=prod
export LOG_LEVEL=WARN
```

### Run in Production
```bash
java -jar target/student-management-api-1.0.0.jar
```

## 📁 Project Structure

```
student-management-api/
├── src/
│   ├── main/
│   │   ├── java/com/student/student/
│   │   │   ├── controller/          # REST controllers
│   │   │   ├── service/             # Business logic
│   │   │   ├── repository/          # Data access layer
│   │   │   ├── entity/              # JPA entities
│   │   │   ├── dto/                 # Data transfer objects
│   │   │   ├── exception/           # Custom exceptions & handlers
│   │   │   └── StudentApplication.java
│   │   └── resources/
│   │       ├── db/migration/        # Flyway migrations
│   │       ├── application.yml      # Main configuration
│   │       └── application-dev.yml  # Development configuration
│   └── test/                        # Test classes
├── docs/                            # Generated documentation
├── logs/                            # Application logs
├── target/                          # Build artifacts
├── Makefile                         # Development commands
├── pom.xml                          # Maven configuration
└── README.md                        # This file
```

## 🛡️ Security Notes

- **Input Validation** - All request data validated
- **SQL Injection Prevention** - JPA/Hibernate protects against SQL injection
- **Error Information** - Production errors don't expose internal details
- **Database Credentials** - Never hardcoded, use environment variables

## 🤝 Contributing

### Development Setup
1. **Fork** the repository
2. **Clone** your fork
3. **Create** feature branch: `git checkout -b feature/new-feature`
4. **Run** `make quickstart` for setup
5. **Make** changes and **test**: `make dev`
6. **Commit** with descriptive message
7. **Push** and create **Pull Request**

### Code Standards
- Follow **Java naming conventions**
- **Write tests** for new features
- **Update documentation** as needed
- **Use descriptive commit messages**

### Running Quality Checks
```bash
make lint              # Static code analysis
make format            # Code formatting
make test              # Run all tests
```

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Health Monitoring**: http://localhost:8080/healthcheck
- **Issues**: Create GitHub issue with detailed description
- **Questions**: Check existing documentation and API examples

---

**Happy Coding!** 🚀

For questions or contributions, please refer to the API documentation and feel free to open an issue.