package com.student.student.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Student request data")
public record StudentRequest(
    @Schema(description = "Student's First Name", example = "John")
    @NotBlank(message = "First Name is required")
    @Length(max = 50, message = "First Name cannot be more than 50 characters")
    String firstName,

    @Schema(description = "Student's Last Name", example = "Doe")
    @NotBlank(message = "Last Name is required")
    @Length(max = 50, message = "Last Name cannot be more than 50 characters")
    String lastName,

    @Schema(description = "Student's Email Address", example = "john.doe@test.link")
    @NotBlank(message = "Email is required")
    @Length(max = 255, message = "Email cannot be more than 255 characters")
    @Email(message = "Please enter a valid email")
    String email,

    @NotNull(message = "Date of Birth is Required")
    @Past(message = "Date of Birth must be in the past")
    @Schema(description = "Student's Date of Birth", example = "2015-12-12")
    LocalDate dateOfBirth
) {}
