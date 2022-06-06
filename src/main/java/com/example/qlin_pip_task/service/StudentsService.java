package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentsService {

    private final StudentRepository studentRepository;

    private final StudentResponses studentResponses;

    private final StudentResponses.StudentResponse StudentResponse;



    public StudentResponses getAllStudentsResponse() {

        List<StudentEntity> allStudentsData = studentRepository.findAll();
        // mapper
        studentResponsesAfterMapper;
//        studentResponses.setData();
        return studentResponsesAfterMapper;
    }

    public String save(StudentSubmitRequest studentSubmitRequest){
        /// mapper - studentSubmitRequest
        studentEnityAfterMapper
        StudentEntity studentData = studentRepository.save(studentEnityAfterMapper);
        return studentData.getId().toString();
    }

    public Optional<StudentEntity.Student> getTheStudentResponse(Integer id) {
        studentRepository.findById(id);
        // mapper
        return studentResponses;
    }

}
