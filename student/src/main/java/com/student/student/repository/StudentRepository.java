package com.student.student.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


import com.student.student.entity.Student;


public interface StudentRepository extends JpaRepository<Student,UUID>{

    
    Boolean existsByRollNumber(Integer rollNumber);
    Boolean existsByEmail(String email);

    void deleteByRollNumber(Integer rollNumber);
    Optional<Student> findByRollNumber(Integer rollNumber);

    Optional<Student> findByEmail(String email);

    



    

}
