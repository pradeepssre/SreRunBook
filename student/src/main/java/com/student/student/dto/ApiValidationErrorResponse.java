package com.student.student.dto;




import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "API Validation Error Response")
public record ApiValidationErrorResponse(
 @Schema(description = "Timestamp when the error occurred", example = "2025-08-23T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timestamp,
    
    @Schema(description = "HTTP status code", example = "400")
    int status,
    
    @Schema(description = "Error code", example = "VALIDATION_FAILED")
    String error,
    
    @Schema(description = "General error message", example = "Invalid input data")
    String message,
    
    @Schema(description = "Request path that caused the error", example = "/api/v1/students")
    String path,
    
    @Schema(description = "Field-specific validation errors", 
            example = "{\"email\": \"must be a well-formed email address\", \"firstName\": \"must not be blank\"}")
    Map<String, String> fieldErrors
) {}


