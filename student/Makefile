# Makefile for Student Management API
# Java Spring Boot REST API with PostgreSQL

# Variables
APP_NAME = student-management-api
JAR_FILE = target/$(APP_NAME)-1.0.0.jar
MAIN_CLASS = com.student.student.StudentApplication
MAVEN_OPTS = -Dmaven.test.skip=false

# Default Java and Maven commands
JAVA = java
MAVEN = mvn

# Default profile and port
PROFILE = dev
PORT = 8080

# Colors for output
GREEN = \033[0;32m
YELLOW = \033[1;33m
RED = \033[0;31m
NC = \033[0m # No Color

.PHONY: help clean build test run run-prod package install lint format docs swagger-export postman-generate db-migrate db-status health-check stop

# Default target
all: clean build test

# Help target
help:
	@echo "$(GREEN)Student Management API - Available Commands:$(NC)"
	@echo ""
	@echo "$(YELLOW)Development:$(NC)"
	@echo "  make install          - Install dependencies and setup project"
	@echo "  make build            - Build the application"
	@echo "  make test             - Run all tests"
	@echo "  make run              - Run application in development mode"
	@echo "  make run-prod         - Run application in production mode"
	@echo "  make clean            - Clean build artifacts"
	@echo ""
	@echo "$(YELLOW)Database:$(NC)"
	@echo "  make db-migrate       - Run Flyway database migrations"
	@echo "  make db-status        - Check migration status"
	@echo ""
	@echo "$(YELLOW)Code Quality:$(NC)"
	@echo "  make lint             - Run code analysis with SpotBugs"
	@echo "  make format           - Format code with Spring Java Format"
	@echo "  make package          - Create JAR package"
	@echo ""
	@echo "$(YELLOW)Documentation:$(NC)"
	@echo "  make docs             - Generate and open API documentation"
	@echo "  make swagger-export   - Export OpenAPI specification"
	@echo "  make postman-generate - Generate Postman collection"
	@echo ""
	@echo "$(YELLOW)Monitoring:$(NC)"
	@echo "  make health-check     - Check application health"
	@echo "  make logs             - Show application logs"
	@echo "  make stop             - Stop running application"

# Install dependencies and setup
install:
	@echo "$(GREEN)Installing dependencies and setting up project...$(NC)"
	$(MAVEN) clean install -DskipTests
	@echo "$(GREEN)✅ Project setup complete!$(NC)"

# Clean build artifacts
clean:
	@echo "$(GREEN)Cleaning build artifacts...$(NC)"
	$(MAVEN) clean
	@rm -rf logs/
	@echo "$(GREEN)✅ Clean complete!$(NC)"

# Build the application
build:
	@echo "$(GREEN)Building application...$(NC)"
	$(MAVEN) compile
	@echo "$(GREEN)✅ Build complete!$(NC)"

# Run all tests
test:
	@echo "$(GREEN)Running tests...$(NC)"
	$(MAVEN) test
	@echo "$(GREEN)✅ Tests complete!$(NC)"

# Run unit tests only
test-unit:
	@echo "$(GREEN)Running unit tests...$(NC)"
	$(MAVEN) test -Dtest="**/*Test"
	@echo "$(GREEN)✅ Unit tests complete!$(NC)"

# Run integration tests only
test-integration:
	@echo "$(GREEN)Running integration tests...$(NC)"
	$(MAVEN) test -Dtest="**/*IT"
	@echo "$(GREEN)✅ Integration tests complete!$(NC)"

# Package the application
package:
	@echo "$(GREEN)Packaging application...$(NC)"
	$(MAVEN) package -DskipTests
	@echo "$(GREEN)✅ JAR created: $(JAR_FILE)$(NC)"

# Run application in development mode
run:
	@echo "$(GREEN)Starting application in development mode...$(NC)"
	@echo "$(YELLOW)Profile: $(PROFILE) | Port: $(PORT)$(NC)"
	@echo "$(YELLOW)API Documentation: http://localhost:$(PORT)/swagger-ui.html$(NC)"
	@echo "$(YELLOW)Health Check: http://localhost:$(PORT)/healthcheck$(NC)"
	$(MAVEN) spring-boot:run -Dspring-boot.run.profiles=$(PROFILE)

# Run application in production mode
run-prod:
	@echo "$(GREEN)Starting application in production mode...$(NC)"
	@$(MAKE) package
	$(JAVA) -jar $(JAR_FILE) --spring.profiles.active=prod

# Run application with custom profile
run-profile:
	@echo "$(GREEN)Starting application with profile: $(PROFILE)...$(NC)"
	$(MAVEN) spring-boot:run -Dspring-boot.run.profiles=$(PROFILE)

# Database migrations
db-migrate:
	@echo "$(GREEN)Running database migrations...$(NC)"
	$(MAVEN) flyway:migrate
	@echo "$(GREEN)✅ Database migrations complete!$(NC)"

# Check migration status
db-status:
	@echo "$(GREEN)Checking migration status...$(NC)"
	$(MAVEN) flyway:info

# Clean and rebuild database
db-reset:
	@echo "$(YELLOW)⚠️  This will drop and recreate the database!$(NC)"
	@read -p "Are you sure? (y/N): " confirm && [ "$$confirm" = "y" ]
	$(MAVEN) flyway:clean flyway:migrate
	@echo "$(GREEN)✅ Database reset complete!$(NC)"

# Code formatting
format:
	@echo "$(GREEN)Formatting code...$(NC)"
	$(MAVEN) spring-javaformat:apply
	@echo "$(GREEN)✅ Code formatted!$(NC)"

# Lint/Static analysis
lint:
	@echo "$(GREEN)Running static code analysis...$(NC)"
	$(MAVEN) spotbugs:check
	@echo "$(GREEN)✅ Static analysis complete!$(NC)"

# Generate documentation
docs:
	@echo "$(GREEN)Opening API documentation...$(NC)"
	@echo "$(YELLOW)Starting application and opening Swagger UI...$(NC)"
	@$(MAKE) health-check || (echo "$(RED)Application not running. Starting...$(NC)" && $(MAKE) run &)
	@sleep 3
	@command -v open >/dev/null 2>&1 && open http://localhost:$(PORT)/swagger-ui.html || \
	 command -v xdg-open >/dev/null 2>&1 && xdg-open http://localhost:$(PORT)/swagger-ui.html || \
	 echo "$(YELLOW)Open manually: http://localhost:$(PORT)/swagger-ui.html$(NC)"

# Export Swagger/OpenAPI specification
swagger-export:
	@echo "$(GREEN)Exporting OpenAPI specification...$(NC)"
	@mkdir -p docs
	@curl -s http://localhost:$(PORT)/api-docs > docs/api-spec.json || \
	 (echo "$(RED)❌ Failed to export. Is the application running?$(NC)" && exit 1)
	@echo "$(GREEN)✅ OpenAPI spec exported to docs/api-spec.json$(NC)"

# Generate Postman collection
postman-generate: swagger-export
	@echo "$(GREEN)Generating Postman collection...$(NC)"
	@command -v npx >/dev/null 2>&1 || (echo "$(RED)❌ Node.js/npm required for Postman generation$(NC)" && exit 1)
	@npx openapi-to-postmanv2 -s docs/api-spec.json -o docs/$(APP_NAME).postman_collection.json || \
	 (echo "$(YELLOW)Installing openapi-to-postmanv2...$(NC)" && \
	  npm install -g openapi-to-postmanv2 && \
	  npx openapi-to-postmanv2 -s docs/api-spec.json -o docs/$(APP_NAME).postman_collection.json)
	@echo "$(GREEN)✅ Postman collection generated: docs/$(APP_NAME).postman_collection.json$(NC)"

# Health check
health-check:
	@echo "$(GREEN)Checking application health...$(NC)"
	@curl -f -s http://localhost:$(PORT)/healthcheck/simple > /dev/null && \
	 echo "$(GREEN)✅ Application is healthy!$(NC)" || \
	 echo "$(RED)❌ Application is not responding$(NC)"

# Detailed health check
health-detailed:
	@echo "$(GREEN)Detailed health check...$(NC)"
	@curl -s http://localhost:$(PORT)/healthcheck | python -m json.tool || \
	 curl -s http://localhost:$(PORT)/healthcheck

# Show application logs
logs:
	@echo "$(GREEN)Showing application logs...$(NC)"
	@tail -f logs/student-api.log 2>/dev/null || \
	 echo "$(YELLOW)Log file not found. Application may not be running or logging to console.$(NC)"

# Stop application (if running via make run)
stop:
	@echo "$(GREEN)Stopping application...$(NC)"
	@pkill -f "spring-boot:run" || echo "$(YELLOW)No running Spring Boot application found$(NC)"

# Development workflow - clean, build, test, run
dev: clean build test run

# CI workflow - clean, build, test, package
ci: clean build test package

# Full release workflow
release: clean build test package swagger-export postman-generate
	@echo "$(GREEN)✅ Release artifacts ready!$(NC)"
	@echo "$(YELLOW)Generated files:$(NC)"
	@echo "  - $(JAR_FILE)"
	@echo "  - docs/api-spec.json"
	@echo "  - docs/$(APP_NAME).postman_collection.json"

# Quick start for new developers
quickstart:
	@echo "$(GREEN)🚀 Quick Start Guide$(NC)"
	@echo "$(YELLOW)1. Setup:$(NC)    make install"
	@echo "$(YELLOW)2. Build:$(NC)    make build"
	@echo "$(YELLOW)3. Test:$(NC)     make test"
	@echo "$(YELLOW)4. Run:$(NC)      make run"
	@echo "$(YELLOW)5. Docs:$(NC)     make docs"
	@echo ""
	@echo "$(GREEN)Starting setup...$(NC)"
	@$(MAKE) install

# Watch for changes and rebuild (requires entr)
watch:
	@echo "$(GREEN)Watching for changes... (requires 'entr')$(NC)"
	@find src -name "*.java" | entr -r make build