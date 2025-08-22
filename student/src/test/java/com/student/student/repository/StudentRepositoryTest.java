package com.student.student.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.hibernate.exception.ConstraintViolationException; 
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.student.student.entity.Student;

@DataJpaTest
@ActiveProfiles("test")
@Testcontainers 
public class StudentRepositoryTest {

    
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent;

    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setup(){
        testStudent = Student.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.of(2015,12,12))
                        .email("testemail@test.link")
                        .rollNumber(2)
                        .build();
    }

    

    @Test
    void testDeleteByRollNumber() {
        Student savedStudent = testEntityManager.persistAndFlush(testStudent);
        Integer rollNumber = savedStudent.getRollNumber();

        studentRepository.deleteByRollNumber(rollNumber);

        Boolean exists = studentRepository.existsByRollNumber(rollNumber);
        assertThat(exists).isFalse();


    }

    @Test
    void testExistsByEmail() {
        Student savedStudent = testEntityManager.persistAndFlush(testStudent);
        String email = savedStudent.getEmail();

        Boolean exists = studentRepository.existsByEmail(email);
        assertThat(exists).isTrue();

    }

    @Test
    void testExistsByRollNumber() {
        Student savedStudent = testEntityManager.persistAndFlush(testStudent);
        Integer rollNumber= savedStudent.getRollNumber();

        Boolean exists = studentRepository.existsByRollNumber(rollNumber);

        assertThat(exists).isTrue();

    }

    @Test
    void testFindMaxRollNumber(){
         Student newStudent = Student.builder()
                                .firstName("Jane")
                                .lastName("Doe")
                                .email("jane@test.link")
                                .rollNumber(24)
                                .dateOfBirth(LocalDate.of(2015,12, 12))
                                .build();
        testEntityManager.persistAndFlush(newStudent);
        testEntityManager.persistAndFlush(testStudent);
        Optional<Integer> maxRollNumber = studentRepository.findMaxRollNumber();
        assertThat(maxRollNumber).isPresent();
        assertThat(maxRollNumber.get()).isEqualTo(24);

    }

    @Test
    void testFindByEmail(){
        Student savedStudent = testEntityManager.persistAndFlush(testStudent);
        String email = savedStudent.getEmail();

        Optional<Student> retStudent = studentRepository.findByEmail(email);
        assertThat(retStudent).isNotEmpty();
        assertThat(retStudent.get()).isEqualTo(savedStudent);

    }
    @Test
void testSaveDuplicateEmailThrowsException() {
    // Given - Save first student
    testEntityManager.persistAndFlush(testStudent);
    
    // Create student with same email
    Student duplicateEmailStudent = Student.builder()
        .firstName("Jane")
        .lastName("Smith")
        .rollNumber(3)  // Different roll number
        .email("testemail@test.link")  // 
        .dateOfBirth(LocalDate.of(2000, 1, 1))
        .build();
    
    // When & Then - Exception thrown when we flush to database
    assertThatThrownBy(() -> {
        studentRepository.save(duplicateEmailStudent);
        testEntityManager.flush();  // ✅ This forces DB write and throws exception
    }).isInstanceOf(ConstraintViolationException.class);
}

@Test
void testSaveDuplicateRollNumberThrowsException() {
    // Given
    testEntityManager.persistAndFlush(testStudent);
    
    // Create student with same roll number
    Student duplicateRollStudent = Student.builder()
        .firstName("Jane")
        .lastName("Smith")
        .rollNumber(2)  //  Same roll number
        .email("jane@different.email")  // Different email
        .dateOfBirth(LocalDate.of(2000, 1, 1))
        .build();
    
    // When & Then
    assertThatThrownBy(() -> {
        studentRepository.save(duplicateRollStudent);
        testEntityManager.flush();  // ✅ Forces DB constraint check
    }).isInstanceOf(ConstraintViolationException.class);
}
}
