package com.student.student.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Student response data")
public record StudentResponse(
    @Schema(description = "Student's unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID studentId,
    @Schema(description = "Student's Auto Gnerated Roll Number", example = "1001")
    Integer rollNumber,
    @Schema(description = "Student's First Name", example = "John")
    String firstName,
    @Schema(description = "Student's Last Name", example = "Doe")
    String lastName,
    @Schema(description = "Student's Email Address", example = "john.doe@test.link")
    String email,
    @Schema(description = "Student's Date of Birth", example = "2015-12-12")
    LocalDate dateOfBirth,
    @Schema(description = "Record creation timestamp", example = "2023-10-01T12:00:00") 
    LocalDateTime createdAt,
    @Schema(description = "Record last update timestamp", example = "2023-10-02T15:30:00")
    LocalDateTime updatedAt

) {

}
