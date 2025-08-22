package com.student.student.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


import com.student.student.entity.Student;
import com.student.student.exception.EmailAlreadyExistsException;
import com.student.student.exception.StudentNotFoundException;
import com.student.student.repository.StudentRepository;
import com.student.student.dto.StudentRequest;
import com.student.student.dto.StudentResponse;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentResponse createStudent(StudentRequest request)
    {
        Boolean exists = studentRepository.existsByEmail(request.email());
        if (exists)
        {
            log.error(String.format("Email : {} already exists", request.email()));
            throw new EmailAlreadyExistsException(String.format("Email %s already exists", request.email())) ;
        }

        Student student = Student.builder()
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .email(request.email())
                        .dateOfBirth(request.dateOfBirth())
                        .build();
        student = studentRepository.save(student);
        log.info(String.format("Student created with id : {} and Roll Number {}",student.getStudentId(),student.getRollNumber()));

        return new StudentResponse(student.getStudentId(),
        student.getRollNumber(),student.getFirstName(),
        student.getLastName(),student.getEmail(),student.getDateOfBirth()
        ,student.getCreatedAt(),student.getUpdatedAt());   


    }

    public StudentResponse getStudentByEmail(String email)
    {
        Student student = studentRepository.findByEmail(email)
                        .orElseThrow(()-> new StudentNotFoundException(
                            String.format("Student with email %s not found",email)
                        ));
        return new StudentResponse(student.getStudentId(),
        student.getRollNumber(),student.getFirstName(),
        student.getLastName(),student.getEmail(),student.getDateOfBirth()
        ,student.getCreatedAt(),student.getUpdatedAt());   
    }

    public StudentResponse getStudentByRollNumber(Integer rollNumber)
    {
        Student student = studentRepository.findByRollNumber(rollNumber)
                        .orElseThrow(()-> new StudentNotFoundException(
                            String.format("Student with roll number %d not found",rollNumber)
                        ));
        log.info(String.format("Student with roll number %d found",rollNumber));
        return new StudentResponse(student.getStudentId(),
        student.getRollNumber(),student.getFirstName(),
        student.getLastName(),student.getEmail(),student.getDateOfBirth()
        ,student.getCreatedAt(),student.getUpdatedAt());   
    }   

    public StudentResponse getStudentById(UUID studentId)
    {
        Student student = studentRepository.findById(studentId  )
                        .orElseThrow(()-> new StudentNotFoundException(
                            String.format("Student with id %s not found",studentId)
                        ));
        log.info(String.format("Student with id %s found",studentId));
        return new StudentResponse(student.getStudentId(),
        student.getRollNumber(),student.getFirstName(),
        student.getLastName(),student.getEmail(),student.getDateOfBirth()
        ,student.getCreatedAt(),student.getUpdatedAt());
    }

    public void deleteStudentByRollNumber(Integer rollNumber)
    {
        studentRepository.deleteByRollNumber(rollNumber);
        log.info(String.format("Student with roll number %d deleted",rollNumber));
    }

    public void deleteStudentById(UUID studentId)
    {
        studentRepository.deleteById(studentId);
        log.info(String.format("Student with id %s deleted",studentId));
    }

    public StudentResponse updateStudent(UUID studentId, StudentRequest request)
    {
        Student student = studentRepository.findById(studentId)
                        .orElseThrow(()-> new StudentNotFoundException(
                            String.format("Student with id %s not found",studentId)
                        )); 
        if(student.getEmail().equals(request.email()))
        {
            student.setFirstName(request.firstName());
            student.setLastName(request.lastName());
            student.setDateOfBirth(request.dateOfBirth());
            student = studentRepository.save(student);
            log.info(String.format("Updated other details except email for student with id %s",studentId));
            return new StudentResponse(student.getStudentId(),
            student.getRollNumber(),student.getFirstName(),
            student.getLastName(),student.getEmail(),student.getDateOfBirth()
            ,student.getCreatedAt(),student.getUpdatedAt());
        } else {
            Boolean exists = studentRepository.existsByEmail(request.email());
            if (exists)
            {
                log.error(String.format("Email : {} already exists", request.email()));
                throw new EmailAlreadyExistsException(String.format("Email %s already exists", request.email())) ;
            } 
            student.setFirstName(request.firstName());
            student.setLastName(request.lastName());
            student.setDateOfBirth(request.dateOfBirth());
            student.setEmail(request.email());
            student = studentRepository.save(student);
            log.info(String.format("Updated all details including email for student with id %s",studentId));
            return new StudentResponse(student.getStudentId(),
            student.getRollNumber(),student.getFirstName(),
            student.getLastName(),student.getEmail(),student.getDateOfBirth()
            ,student.getCreatedAt(),student.getUpdatedAt());
        }
        
}

public List<StudentResponse> getAllStudents()
{
    List<Student> students = studentRepository.findAll();
    return students.stream().map(student -> new StudentResponse(
        student.getStudentId(),
        student.getRollNumber(),
        student.getFirstName(),
        student.getLastName(),
        student.getEmail(),
        student.getDateOfBirth(),
        student.getCreatedAt(),
        student.getUpdatedAt()
    )).collect(Collectors.toList());            

}

}