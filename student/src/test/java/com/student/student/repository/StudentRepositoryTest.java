package com.student.student.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
// @Test
// void testExistsByRollNumber() {
//     // Given - Create student WITHOUT setting roll number
//     Student studentForTest = Student.builder()
//             .firstName("John")
//             .lastName("Doe")
//             .dateOfBirth(LocalDate.of(2000, 12, 12))
//             .email("testemail@test.link")
//             .build();
    
//     Student savedStudent = testEntityManager.persistAndFlush(studentForTest);
//     assertThat(savedStudent.getRollNumber()).isNotNull();
//     Integer actualRollNumber = savedStudent.getRollNumber();  // Get the DB-generated roll number
    
//     // When & Then
    
//     assertThat(studentRepository.existsByRollNumber(actualRollNumber)).isTrue();
//     assertThat(studentRepository.existsByRollNumber(999)).isFalse();
// }



    @Test
    void testFindByEmail(){
        Student savedStudent = testEntityManager.persistAndFlush(testStudent);
        String email = savedStudent.getEmail();

        Optional<Student> retStudent = studentRepository.findByEmail(email);
        assertThat(retStudent).isNotEmpty();
        assertThat(retStudent.get()).isEqualTo(savedStudent);

    }
    


}
