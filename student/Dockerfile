FROM maven:3.9.11-eclipse-temurin-21-noble AS builder
WORKDIR /app

COPY pom.xml .

COPY src/ ./src
RUN mvn package -DskipTests -B

RUN ls -la target/ && test -f target/*.jar

FROM eclipse-temurin:21-jre-alpine-3.22 AS runtime

RUN apk add --no-cache curl && \
    addgroup -g 1001 appgroup && \
    adduser -u 1001 -G appgroup -s /bin/sh -D appuser

# Set working directory
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar
RUN chown -R appuser:appgroup /app

USER appuser
EXPOSE 8080

# Health check using your existing endpoint
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/healthcheck || exit 1

# JVM optimization for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
