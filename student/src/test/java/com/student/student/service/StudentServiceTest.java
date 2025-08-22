package com.student.student.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.context.ActiveProfiles;

import com.student.student.dto.StudentRequest;
import com.student.student.dto.StudentResponse;
import com.student.student.entity.Student;
import com.student.student.exception.EmailAlreadyExistsException;
import com.student.student.exception.StudentNotFoundException;
import com.student.student.repository.StudentRepository;



@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)

public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;  // ✅ Mock the repository
    
    @InjectMocks
    private StudentService studentService;
    private StudentRequest testRequest;
    private Student testStudent;
    private UUID testStudentId;

    @BeforeEach
    void setUp() {
        testStudentId = UUID.randomUUID();
        
        testRequest = new StudentRequest(
                "John",
                "Doe", 
                "john.doe@test.com",
                LocalDate.of(2000, 1, 15)
        );

        testStudent = Student.builder()
                .studentId(testStudentId)
                .rollNumber(1001)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .dateOfBirth(LocalDate.of(2000, 1, 15))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateStudent() {
        when(studentRepository.existsByEmail("john.doe@test.com")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // When
        StudentResponse result = studentService.createStudent(testRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.firstName()).isEqualTo("John");
        assertThat(result.lastName()).isEqualTo("Doe");
        assertThat(result.email()).isEqualTo("john.doe@test.com");
        assertThat(result.rollNumber()).isEqualTo(1001);
        assertThat(result.studentId()).isEqualTo(testStudentId);
        assertThat(result.dateOfBirth()).isEqualTo(LocalDate.of(2000, 1, 15));
        assertThat(result.createdAt()).isNotNull();
        assertThat(result.updatedAt()).isNotNull();

        // Verify interactions
        verify(studentRepository).existsByEmail("john.doe@test.com");
        verify(studentRepository).save(any(Student.class));

    }

    @Test
    void testCreateStudent_Failure() {
        when(studentRepository.existsByEmail("john.doe@test.com")).thenReturn(true);
        

         assertThatThrownBy(() -> studentService.createStudent(testRequest))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("Email john.doe@test.com already exists");

    }

    @Test
    void testDeleteStudentById() {
       

        // When
        studentService.deleteStudentById(testStudentId);

        // Then
        verify(studentRepository).deleteById(testStudentId);

    }

    @Test
    void testDeleteStudentByRollNumber() {
       studentService.deleteStudentByRollNumber(1001);
    
    // Then
    verify(studentRepository).deleteByRollNumber(1001);
    


    }

    @Test
    void testGetStudentByEmail() {
        when(studentRepository.findByEmail("john.doe@test.com"))
            .thenReturn(Optional.of(testStudent));
        StudentResponse result = studentService.getStudentByEmail("john.doe@test.com");
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("john.doe@test.com");
        assertThat(result.firstName()).isEqualTo("John");
        assertThat(result.lastName()).isEqualTo("Doe");
        assertThat(result.rollNumber()).isEqualTo(1001);
        assertThat(result.studentId()).isEqualTo(testStudentId);
        assertThat(result.dateOfBirth()).isEqualTo(LocalDate.of(2000, 1, 15));
        assertThat(result.createdAt()).isNotNull();
        assertThat(result.updatedAt()).isNotNull();


    }

      @Test
    void testGetStudentByEmail_throwsException() {
        when(studentRepository.findByEmail("john.doe@test.com"))
            .thenThrow(StudentNotFoundException.class);
        
        
        assertThatThrownBy(() -> studentService.getStudentByEmail("john.doe@test.com"))
                .isInstanceOf(StudentNotFoundException.class);


    }

    @Test
    void testGetStudentById() {

        when(studentRepository.findById(testStudentId))
            .thenReturn(java.util.Optional.of(testStudent));
        StudentResponse result = studentService.getStudentById(testStudentId);
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("john.doe@test.com");
        assertThat(result.firstName()).isEqualTo("John");
        assertThat(result.lastName()).isEqualTo("Doe");
        assertThat(result.rollNumber()).isEqualTo(1001);
        assertThat(result.studentId()).isEqualTo(testStudentId);
        assertThat(result.dateOfBirth()).isEqualTo(LocalDate.of(2000, 1, 15));
        assertThat(result.createdAt()).isNotNull();
        assertThat(result.updatedAt()).isNotNull(); 

    }

    @Test
    void testGetStudentById_ThrowsException() {

        when(studentRepository.findById(testStudentId))
            .thenThrow(StudentNotFoundException.class);
        assertThatThrownBy(() -> studentService.getStudentById(testStudentId))
                .isInstanceOf(StudentNotFoundException.class);
        
    }

    @Test
    void testGetStudentByRollNumber() {
        when(studentRepository.findByRollNumber(1001))
            .thenReturn(java.util.Optional.of(testStudent));
        StudentResponse result = studentService.getStudentByRollNumber(1001);
       assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("john.doe@test.com");
        assertThat(result.firstName()).isEqualTo("John");
        assertThat(result.lastName()).isEqualTo("Doe");
        assertThat(result.rollNumber()).isEqualTo(1001);
        assertThat(result.studentId()).isEqualTo(testStudentId);
        assertThat(result.dateOfBirth()).isEqualTo(LocalDate.of(2000, 1, 15));
        assertThat(result.createdAt()).isNotNull();
        assertThat(result.updatedAt()).isNotNull(); 

    }

    @Test
    void testGetStudentByRollNumber_ThrowsException() {
        when(studentRepository.findByRollNumber(1001))
            .thenThrow(StudentNotFoundException.class);
        assertThatThrownBy(() -> studentService.getStudentByRollNumber(1001))
                .isInstanceOf(StudentNotFoundException.class);  
    }

    @Test
    void testUpdateStudent() {   

        StudentRequest updateRequest = new StudentRequest(
               "Jane",
            "Smith",
            "jane.smith@test.com",  // Different email
            LocalDate.of(1999, 5, 20));
        Student updatedStudent = Student.builder()
                .studentId(testStudentId)
                .rollNumber(1001)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@test.com")
                .dateOfBirth(LocalDate.of(1999, 5, 20))
                .createdAt(testStudent.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        when(studentRepository.findById(testStudentId))
            .thenReturn(java.util.Optional.of(testStudent));
        when(studentRepository.existsByEmail("jane.smith@test.com"))
            .thenReturn(false);
        when(studentRepository.save(any(Student.class)))
            .thenReturn(updatedStudent);
        StudentResponse result = studentService.updateStudent(testStudentId, updateRequest);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.firstName()).isEqualTo("Jane");
    assertThat(result.lastName()).isEqualTo("Smith");
    assertThat(result.email()).isEqualTo("jane.smith@test.com");
    assertThat(result.rollNumber()).isEqualTo(1001);  // Roll number unchanged
    assertThat(result.studentId()).isEqualTo(testStudentId);
    assertThat(result.dateOfBirth()).isEqualTo(LocalDate.of(1999, 5, 20));

    // Verify interactions
    verify(studentRepository).findById(testStudentId);
    verify(studentRepository).existsByEmail("jane.smith@test.com");
    verify(studentRepository).save(any(Student.class));
        
        

      }

      @Test
void testUpdateStudent_StudentNotFound_ThrowsException() {
    // Given
    StudentRequest updateRequest = new StudentRequest(
            "Jane",
            "Smith",
            "jane.smith@test.com",
            LocalDate.of(1999, 5, 20)
    );
    
    when(studentRepository.findById(testStudentId)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> studentService.updateStudent(testStudentId, updateRequest))
            .isInstanceOf(StudentNotFoundException.class)
            .hasMessage(String.format("Student with id %s not found", testStudentId));

    // Verify interactions
    verify(studentRepository).findById(testStudentId);
    verify(studentRepository, never()).existsByEmail(any());
    verify(studentRepository, never()).save(any());
}


void testUpdateStudent_SameEmail_Success() {
    // Given - updating with same email (should not check for existence)
    StudentRequest updateRequest = new StudentRequest(
            "John Updated",
            "Doe Updated",
            "john.doe@test.com",  // Same email as existing
            LocalDate.of(2000, 1, 15)
    );
    
    Student updatedStudent = Student.builder()
            .studentId(testStudentId)
            .rollNumber(1001)
            .firstName("John Updated")
            .lastName("Doe Updated")
            .email("john.doe@test.com")
            .dateOfBirth(LocalDate.of(2000, 1, 15))
            .createdAt(testStudent.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();

    when(studentRepository.findById(testStudentId)).thenReturn(Optional.of(testStudent));
    when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

    // When
    StudentResponse result = studentService.updateStudent(testStudentId, updateRequest);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.firstName()).isEqualTo("John Updated");
    assertThat(result.lastName()).isEqualTo("Doe Updated");
    assertThat(result.email()).isEqualTo("john.doe@test.com");

    // Verify interactions
    verify(studentRepository).findById(testStudentId);
    verify(studentRepository, never()).existsByEmail(any());  // Should not check email existence
    verify(studentRepository).save(any(Student.class));
}
    @Test
    void testGetAllStudents() {
        Student anotherStudent = Student.builder()
                .studentId(UUID.randomUUID())
                .rollNumber(1002)
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice.j@test.com")
                .dateOfBirth(LocalDate.of(2001, 2, 25))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        List<Student> students = List.of(testStudent, anotherStudent);
        when(studentRepository.findAll()).thenReturn(students);
        List<StudentResponse> results = studentService.getAllStudents();
        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(2);        
        assertThat(results)
            .extracting(StudentResponse::firstName)
            .containsExactly("John", "Alice");
            
    assertThat(results)
            .extracting(StudentResponse::email)
            .containsExactly("john.doe@test.com", "alice.j@test.com");
            
    assertThat(results)
            .extracting(StudentResponse::rollNumber)
            .containsExactly(1001, 1002);

    // Verify repository interaction
    verify(studentRepository).findAll();

        }

@Test
void testGetAllStudents_EmptyList() {
    // Given
    when(studentRepository.findAll()).thenReturn(Collections.emptyList());

    // When
    List<StudentResponse> result = studentService.getAllStudents();

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEmpty();
    
    verify(studentRepository).findAll();
}

}
