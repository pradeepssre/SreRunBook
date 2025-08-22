package com.student.student.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.student.entity.Student;


public interface StudentRepository extends JpaRepository<Student,UUID>{

    
    Boolean existsByRollNumber(Integer rollNumber);
    Boolean existsByEmail(String email);

    void deleteByRollNumber(Integer rollNumber);

    Optional<Student> findByEmail(String email);

    @Query("SELECT max(s.rollNumber) FROM Student s")
    Optional<Integer> findMaxRollNumber();



    

}
