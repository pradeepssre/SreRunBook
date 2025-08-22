package com.student.student.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.student.repository.StudentRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/healthcheck")
@Slf4j
@Tag(name = "Health Check", description = "Application health monitoring endpoints")
public class HealthCheckController {

    private final StudentRepository studentRepository;
    private final BuildProperties buildProperties;

    public HealthCheckController(StudentRepository studentRepository, 
                                @Autowired(required = false) BuildProperties buildProperties) {
        this.studentRepository = studentRepository;
        this.buildProperties = buildProperties;
    }

    @GetMapping
    @Operation(
        summary = "Application health check",
        description = "Returns the health status of the application including database connectivity"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Application is healthy",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "status": "UP",
                  "timestamp": "2025-08-23T10:30:00",
                  "application": "Student Management API",
                  "version": "1.0.0",
                  "database": {
                    "status": "UP",
                    "connection": "healthy"
                  },
                  "uptime": "2h 15m 30s"
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "503", 
        description = "Application is unhealthy",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "status": "DOWN",
                  "timestamp": "2025-08-23T10:30:00",
                  "application": "Student Management API",
                  "version": "1.0.0",
                  "database": {
                    "status": "DOWN",
                    "connection": "failed",
                    "error": "Connection refused"
                  }
                }
                """
            )
        )
    )
    public ResponseEntity<Map<String, Object>> healthCheck() {
        log.debug("Health check endpoint accessed");
        
        Map<String, Object> healthStatus = new HashMap<>();
        Map<String, Object> databaseStatus = new HashMap<>();
        
        // Check database connectivity
        boolean isDatabaseHealthy = checkDatabaseHealth(databaseStatus);
        
        // Overall health status
        String overallStatus = isDatabaseHealthy ? "UP" : "DOWN";
        
        // Build response
        healthStatus.put("status", overallStatus);
        healthStatus.put("timestamp", LocalDateTime.now());
        healthStatus.put("application", "Student Management API");
        healthStatus.put("version", getApplicationVersion());
        healthStatus.put("database", databaseStatus);
        
        // Add uptime if available
        healthStatus.put("uptime", getUptime());
        
        if (isDatabaseHealthy) {
            log.debug("Health check passed - all systems operational");
            return ResponseEntity.ok(healthStatus);
        } else {
            log.warn("Health check failed - database connectivity issues");
            return ResponseEntity.status(503).body(healthStatus);
        }
    }

    @GetMapping("/simple")
    @Operation(
        summary = "Simple health check",
        description = "Returns a simple OK status for basic monitoring"
    )
    @ApiResponse(responseCode = "200", description = "Service is running")
    public ResponseEntity<Map<String, String>> simpleHealthCheck() {
        log.debug("Simple health check accessed");
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Service is running");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/database")
    @Operation(
        summary = "Database health check",
        description = "Returns detailed database connectivity status"
    )
    @ApiResponse(responseCode = "200", description = "Database is healthy")
    @ApiResponse(responseCode = "503", description = "Database is unhealthy")
    public ResponseEntity<Map<String, Object>> databaseHealthCheck() {
        log.debug("Database health check accessed");
        
        Map<String, Object> databaseStatus = new HashMap<>();
        boolean isHealthy = checkDatabaseHealth(databaseStatus);
        
        if (isHealthy) {
            return ResponseEntity.ok(databaseStatus);
        } else {
            return ResponseEntity.status(503).body(databaseStatus);
        }
    }

    private boolean checkDatabaseHealth(Map<String, Object> databaseStatus) {
        try {
            // Test database connectivity by counting students
            long studentCount = studentRepository.count();
            
            databaseStatus.put("status", "UP");
            databaseStatus.put("connection", "healthy");
            databaseStatus.put("studentCount", studentCount);
            
            log.debug("Database health check passed - {} students in database", studentCount);
            return true;
            
        } catch (Exception e) {
            databaseStatus.put("status", "DOWN");
            databaseStatus.put("connection", "failed");
            databaseStatus.put("error", e.getMessage());
            
            log.error("Database health check failed: {}", e.getMessage());
            return false;
        }
    }

    private String getApplicationVersion() {
        if (buildProperties != null) {
            return buildProperties.getVersion();
        }
        return "development";
    }

    private String getUptime() {
        // Simple uptime calculation (since JVM started)
        long uptimeMs = System.currentTimeMillis() - 
                       java.lang.management.ManagementFactory.getRuntimeMXBean().getStartTime();
        
        long seconds = uptimeMs / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes % 60, seconds % 60);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds % 60);
        } else {
            return String.format("%ds", seconds);
        }
    }
}