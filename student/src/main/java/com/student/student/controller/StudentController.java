package com.student.student.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.student.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;


import com.student.student.dto.StudentResponse;
import com.student.student.dto.StudentRequest;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;






@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    @Operation(summary = "Get all students", description = "Fetches all student records")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class)))

    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        log.info("Fetching all students");
        
        List<StudentResponse> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Fetches a student record by its unique ID")
    @ApiResponse(responseCode = "200", description = "Student found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class)))
    @ApiResponse(responseCode = "404", description = "Student not found")
    public ResponseEntity<StudentResponse> getStudentById(@Parameter(description = "Unique Id of the student",required = true) @PathVariable("id") UUID studentId) {
        log.info("Fetching student with ID: {}", studentId);
        StudentResponse student = studentService.getStudentById(studentId);
        return ResponseEntity.ok(student);
        

    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get student by email", description = "Fetches a student record by email")
    @ApiResponse(responseCode = "200", description = "Student found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class)))
    @ApiResponse(responseCode = "404", description = "Student not found")   
    public ResponseEntity<StudentResponse> getStudentByEmail(@Parameter(description = "Email Address of the student",required = true) @PathVariable("email") String email) {
        log.info("Fetching student with email: {}", email);
        StudentResponse student = studentService.getStudentByEmail(email);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/rollnumber/{rollNumber}")
    @Operation(summary = "Get student by roll number", description = "Fetches a student record by roll number")
    @ApiResponse(responseCode = "200", description = "Student found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class)))
    @ApiResponse(responseCode = "404", description = "Student not found")   
    public ResponseEntity<StudentResponse> getStudentByRollNumber(@Parameter(description = "Roll Number",required = true) @PathVariable("rollNumber") Integer rollNumber) {
        log.info("Fetching student with roll number: {}", rollNumber);
        StudentResponse student = studentService.getStudentByRollNumber(rollNumber);
        return ResponseEntity.ok(student);
    }   
    
    @PostMapping
    @Operation(summary = "Create a new student", description = "Creates a new student record")
    @ApiResponse(responseCode = "201", description = "Student created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "409", description = "Email already exists")
    
    public ResponseEntity<StudentResponse> createStudent(
    @Parameter(description = "Student details", required = true)
    @Valid @RequestBody StudentRequest studentRequest) {
    
    log.info("Creating new student with email: {}", studentRequest.email());
    StudentResponse createdStudent = studentService.createStudent(studentRequest);
    log.info("Successfully created student with ID: {} and roll number: {}", 
                createdStudent.studentId(), createdStudent.rollNumber());
    
    return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }    

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing student", description = "Updates an existing student record by ID")
    @ApiResponse(responseCode = "200", description = "Student updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class)))  
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Student not found")
    @ApiResponse(responseCode = "409", description = "Email already exists")
    public ResponseEntity<StudentResponse> updateStudent(
        @Parameter(description = "Unique Id of the student to be updated", required = true) @PathVariable("id") UUID studentId,
        @Parameter(description = "Updated student details", required = true) @Valid @RequestBody StudentRequest studentRequest) {
        
        log.info("Updating student with ID: {}", studentId);
        StudentResponse updatedStudent = studentService.updateStudent(studentId, studentRequest);
        log.info("Successfully updated student with ID: {}", updatedStudent.studentId());
        return ResponseEntity.ok(updatedStudent);
    }   

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a student", description = "Deletes a student record by ID")
    @ApiResponse(responseCode = "204", description = "Student deleted successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    public ResponseEntity<Void> deleteStudent(
        @Parameter(description = "Unique Id of the student to be deleted", required = true) @PathVariable("id") UUID studentId) {
        log.info("Deleting student with ID: {}", studentId);
        studentService.deleteStudentById(studentId);
        log.info("Successfully deleted student with ID: {}", studentId);
        return ResponseEntity.noContent().build();
    
    }
    
    @DeleteMapping("/rollnumber/{rollNumber}")
    @Operation(summary = "Delete a student by roll number", description = "Deletes a student record by roll number")
    @ApiResponse(responseCode = "204", description = "Student deleted successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    public ResponseEntity<Void> deleteStudentByRollNumber(
        @Parameter(description = "Roll Number of the student to be deleted", required = true) @PathVariable("rollNumber") Integer rollNumber) {
        log.info("Deleting student with roll number: {}", rollNumber);
        studentService.deleteStudentByRollNumber(rollNumber);
        log.info("Successfully deleted student with roll number: {}", rollNumber);
        return ResponseEntity.noContent().build();  
        
    }

    

}
