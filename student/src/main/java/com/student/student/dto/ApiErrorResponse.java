package com.student.student.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "API Error Response")

public record ApiErrorResponse(
 @Schema(description = "Timestamp of the error", example = "2024-10-01T12:34:56")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timestamp ,
    @Schema(description = "HTTP status code", example = "404")
    int status,
    
    @Schema(description = "Error code", example = "STUDENT_NOT_FOUND")
    String error,
    
    @Schema(description = "Error message", example = "Student with id 123e4567-e89b-12d3-a456-426614174000 not found")
    String message,
    
    @Schema(description = "Request path that caused the error", example = "/api/v1/students/123e4567-e89b-12d3-a456-426614174000")
    String path


) {}
   


