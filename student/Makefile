# Makefile for Student Management API
# Java Spring Boot REST API with PostgreSQL

# Variables
APP_NAME = student-management-api
JAR_FILE = target/$(APP_NAME)-1.0.0.jar
MAIN_CLASS = com.student.student.StudentApplication
MAVEN_OPTS = -Dmaven.test.skip=false

# Docker variables
DOCKER_IMAGE = student-api
DOCKER_TAG = latest
COMPOSE_APP_ONLY = docker-compose.app.yml
COMPOSE_STACK = docker-compose.stack.yml
COMPOSE_PROD = docker-compose.prod.yml

#environment  variables files
ENV_APP_ONLY = .env.docker-app
ENV_STACK = .env.docker-stack
ENV_PROD = .env.docker-app

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
CYAN = \033[0;36m
NC = \033[0m # No Color

.PHONY: help clean build test run run-prod package install lint format docs swagger-export postman-generate db-migrate db-status health-check stop docker-* docker-stack docker-stack-build docker-stack-up docker-stack-down docker-stack-logs docker-stack-clean

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
	@echo "$(YELLOW)Docker - Build & Management:$(NC)"
	@echo "  make docker-build     - Build optimized Docker image"
	@echo "  make docker-clean     - Remove Docker containers and images"
	@echo "  make docker-prune     - Clean up unused Docker resources"
	@echo ""
	@echo "$(YELLOW)Docker - Scenarios:$(NC)"
	@echo "  make docker-app-only  - Run app container + local database"
	@echo "  make docker-stack     - Run full stack (app + PostgreSQL)"
	@echo "  make docker-prod      - Run production app + external database"
	@echo ""
	@echo "$(YELLOW)Docker - Operations:$(NC)"
	@echo "  make docker-logs      - View container logs"
	@echo "  make docker-stop      - Stop running containers"
	@echo "  make docker-status    - Show container status"
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
	$(MAVEN) flyway:migrate \
		-Dflyway.url=$(DATABASE_URL) \
		-Dflyway.user=$(DATABASE_USERNAME) \
		-Dflyway.password=$(DATABASE_PASSWORD) \
		-Dflyway.locations=filesystem:src/main/resources/db/migration
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

# =====================================================
# DOCKER SECTION - NEW CONTAINERIZATION COMMANDS
# =====================================================

# Docker - Build optimized container image
docker-build:
	@echo "$(GREEN)Building Docker image: $(DOCKER_IMAGE):$(DOCKER_TAG)...$(NC)"
	docker build -t $(DOCKER_IMAGE):$(DOCKER_TAG) .
	@echo "$(GREEN)✅ Docker image built successfully!$(NC)"
	@echo "$(CYAN)Image size:$(NC)"
	@docker images $(DOCKER_IMAGE):$(DOCKER_TAG) --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}"

# Docker - Build without cache
docker-build-fresh:
	@echo "$(GREEN)Building Docker image (fresh build)...$(NC)"
	docker build --no-cache -t $(DOCKER_IMAGE):$(DOCKER_TAG) .
	@echo "$(GREEN)✅ Fresh Docker image built!$(NC)"

# Docker Scenario 1: App container + Local database
docker-app-only: docker-build
	@echo "$(GREEN)Starting app container with local database...$(NC)"
	@echo "$(CYAN)Profile: docker-app | Database: Local PostgreSQL$(NC)"
	docker-compose --env-file ${ENV_APP_ONLY} -f $(COMPOSE_APP_ONLY) up -d
	@echo "$(GREEN)✅ App container started!$(NC)"
	@echo "$(CYAN)🌐 API: http://localhost:8080$(NC)"
	@echo "$(CYAN)📚 Swagger: http://localhost:8080/swagger-ui.html$(NC)"
	@echo "$(YELLOW)💡 Connects to your local PostgreSQL database$(NC)"

# Docker Scenario 2: Full stack (app + database + pgAdmin)
docker-stack: docker-build
	@echo "$(GREEN)Starting full Docker stack...$(NC)"
	@echo "$(CYAN)Profile: docker-stack | Database: Docker PostgreSQL$(NC)"
	docker-compose --env-file ${ENV_STACK} -f $(COMPOSE_STACK) up 
	@echo "$(GREEN)✅ Full stack started!$(NC)"
	@echo "$(CYAN)🌐 API: http://localhost:8080$(NC)"
	@echo "$(CYAN)📚 Swagger: http://localhost:8080/swagger-ui.html$(NC)"
	@echo "$(CYAN)🗄️  pgAdmin: http://localhost:5050$(NC)"
	@echo "$(YELLOW)💡 Login: admin@student.local / admin123$(NC)"

docker-stack-down:
	@echo "$(YELLOW)Stopping full Docker stack...$(NC)"
	docker-compose --env-file $(ENV_STACK) -f $(COMPOSE_STACK) down

docker-stack-logs:
	@echo "$(CYAN)Showing Docker stack logs...$(NC)"
	docker-compose --env-file $(ENV_STACK) -f $(COMPOSE_STACK) logs -f

docker-stack-clean:
	@echo "$(RED)Cleaning Docker stack...$(NC)"
	docker-compose --env-file $(ENV_STACK) -f $(COMPOSE_STACK) down -v

# Docker Scenario 3: Production app + External database
docker-prod: docker-build
	@echo "$(GREEN)Starting production app container...$(NC)"
	@echo "$(CYAN)Profile: docker-prod | Database: External (RDS/Cloud SQL)$(NC)"
	@echo "$(RED)⚠️  Ensure DB_HOST and credentials are set in .env!$(NC)"
	docker-compose --env-file ${ENV_PROD}  -f $(COMPOSE_PROD) up -d
	@echo "$(GREEN)✅ Production app started!$(NC)"
	@echo "$(CYAN)🌐 API: http://localhost:8080$(NC)"

# Docker - View logs for different scenarios
docker-logs:
	@echo "$(GREEN)Docker container logs:$(NC)"
	@echo "Choose scenario: [1] App-only [2] Stack [3] Production"
	@read -p "Enter choice (1-3): " choice; \
	case $$choice in \
		1) docker-compose -f $(COMPOSE_APP_ONLY) logs -f ;; \
		2) docker-compose -f $(COMPOSE_STACK) logs -f ;; \
		3) docker-compose -f $(COMPOSE_PROD) logs -f ;; \
		*) echo "$(RED)Invalid choice$(NC)" ;; \
	esac

# Docker - Logs for specific scenarios
docker-logs-app:
	@echo "$(GREEN)App-only container logs:$(NC)"
	docker-compose -f $(COMPOSE_APP_ONLY) logs -f

docker-logs-stack:
	@echo "$(GREEN)Full stack logs:$(NC)"
	docker-compose -f $(COMPOSE_STACK) logs -f

docker-logs-prod:
	@echo "$(GREEN)Production container logs:$(NC)"
	docker-compose -f $(COMPOSE_PROD) logs -f

# Docker - Stop containers
docker-stop:
	@echo "$(GREEN)Stopping Docker containers...$(NC)"
	@docker-compose -f $(COMPOSE_APP_ONLY) stop 2>/dev/null || true
	@docker-compose -f $(COMPOSE_STACK) stop 2>/dev/null || true
	@docker-compose -f $(COMPOSE_PROD) stop 2>/dev/null || true
	@echo "$(GREEN)✅ All containers stopped!$(NC)"

# Docker - Stop specific scenarios
docker-stop-app:
	docker-compose -f $(COMPOSE_APP_ONLY) stop

docker-stop-stack:
	docker-compose -f $(COMPOSE_STACK) stop

docker-stop-prod:
	docker-compose -f $(COMPOSE_PROD) stop

# Docker - Remove containers (keep volumes)
docker-down:
	@echo "$(GREEN)Removing Docker containers...$(NC)"
	@docker-compose -f $(COMPOSE_APP_ONLY) down 2>/dev/null || true
	@docker-compose -f $(COMPOSE_STACK) down 2>/dev/null || true
	@docker-compose -f $(COMPOSE_PROD) down 2>/dev/null || true
	@echo "$(GREEN)✅ All containers removed!$(NC)"

# Docker - Container status
docker-status:
	@echo "$(GREEN)Docker container status:$(NC)"
	@echo "$(CYAN)Running containers:$(NC)"
	@docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" --filter "name=student" 2>/dev/null || echo "No containers running"
	@echo ""
	@echo "$(CYAN)Available images:$(NC)"
	@docker images $(DOCKER_IMAGE) --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}" 2>/dev/null || echo "No images found"

# Docker - Health check for containerized app
docker-health:
	@echo "$(GREEN)Checking Docker container health...$(NC)"
	@curl -f -s http://localhost:8080/healthcheck > /dev/null && \
	 echo "$(GREEN)✅ Containerized app is healthy!$(NC)" || \
	 echo "$(RED)❌ Containerized app is not responding$(NC)"

# Docker - Database backup (for stack scenario)
docker-db-backup:
	@echo "$(GREEN)Creating database backup...$(NC)"
	@docker exec student-postgres-stack pg_dump -U student_user -d studentdb > backup_$$(date +%Y%m%d_%H%M%S).sql 2>/dev/null && \
	 echo "$(GREEN)✅ Database backup created!$(NC)" || \
	 echo "$(RED)❌ Backup failed. Is the stack running?$(NC)"

# Docker - Cleanup images and containers
docker-clean:
	@echo "$(GREEN)Cleaning Docker resources...$(NC)"
	@$(MAKE) docker-down
	@docker rmi $(DOCKER_IMAGE):$(DOCKER_TAG) 2>/dev/null || true
	@echo "$(GREEN)✅ Docker cleanup complete!$(NC)"

# Docker - Remove everything including volumes (⚠️ DATA LOSS!)
docker-clean-all:
	@echo "$(RED)⚠️  This will delete ALL Docker data including volumes!$(NC)"
	@read -p "Are you sure? Type 'yes' to confirm: " confirm && [ "$$confirm" = "yes" ] || (echo "$(GREEN)Cancelled.$(NC)" && exit 1)
	@echo "$(YELLOW)Removing all Docker resources...$(NC)"
	@docker-compose -f $(COMPOSE_APP_ONLY) down -v --rmi local 2>/dev/null || true
	@docker-compose -f $(COMPOSE_STACK) down -v --rmi local 2>/dev/null || true
	@docker-compose -f $(COMPOSE_PROD) down -v --rmi local 2>/dev/null || true
	@echo "$(GREEN)✅ All Docker resources removed!$(NC)"

# Docker - System cleanup
docker-prune:
	@echo "$(GREEN)Cleaning unused Docker resources...$(NC)"
	docker system prune -f
	@echo "$(GREEN)✅ Docker system cleanup complete!$(NC)"

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

# Docker release workflow
docker-release: docker-build package swagger-export postman-generate
	@echo "$(GREEN)✅ Docker release artifacts ready!$(NC)"
	@echo "$(YELLOW)Generated:$(NC)"
	@echo "  - Docker image: $(DOCKER_IMAGE):$(DOCKER_TAG)"
	@echo "  - JAR file: $(JAR_FILE)"
	@echo "  - API docs: docs/api-spec.json"
	@echo "  - Postman: docs/$(APP_NAME).postman_collection.json"

# Quick start for new developers
quickstart:
	@echo "$(GREEN)🚀 Quick Start Guide$(NC)"
	@echo "$(YELLOW)Standard Development:$(NC)"
	@echo "  1. Setup:     make install"
	@echo "  2. Build:     make build"
	@echo "  3. Test:      make test"
	@echo "  4. Run:       make run"
	@echo "  5. Docs:      make docs"
	@echo ""
	@echo "$(YELLOW)Docker Development:$(NC)"
	@echo "  1. Build:     make docker-build"
	@echo "  2. Run Stack: make docker-stack"
	@echo "  3. Check:     make docker-health"
	@echo ""
	@echo "$(GREEN)Starting setup...$(NC)"
	@$(MAKE) install

# Docker quick start
docker-quickstart:
	@echo "$(GREEN)🐳 Docker Quick Start$(NC)"
	@echo "$(YELLOW)Building and starting full Docker stack...$(NC)"
	@$(MAKE) docker-stack
	@sleep 10
	@$(MAKE) docker-health

# Watch for changes and rebuild (requires entr)
watch:
	@echo "$(GREEN)Watching for changes... (requires 'entr')$(NC)"
	@find src -name "*.java" | entr -r make build