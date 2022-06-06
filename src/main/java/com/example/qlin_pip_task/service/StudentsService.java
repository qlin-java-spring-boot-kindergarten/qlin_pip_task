package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.mapper.StudentMapper;
import com.example.qlin_pip_task.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentsService {

    private final StudentRepository studentRepository;

    private final StudentResponses studentResponses;

    private final StudentResponses.StudentResponse StudentResponse;

    private final StudentMapper studentMapper;

    public StudentResponses getAllStudentsResponse() {

        List<StudentEntity> allStudentsData = studentRepository.findAll();
        // mapper
        StudentResponses studentResponses = studentMapper.entitiesToStudentResponses(allStudentsData);
//        studentResponses.setData();
        return studentResponses;
    }

    public String save(StudentSubmitRequest studentSubmitRequest){
        /// mapper - studentSubmitRequest
        StudentEntity studentEntity = studentMapper.requestToStudentEntity(studentSubmitRequest);
        StudentEntity studentData = studentRepository.save(studentEntity);
        return studentData.getId().toString();
    }

    public Optional<StudentResponses.StudentResponse> getTheStudentResponse(Integer id) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(id);
        if (studentEntity.isEmpty()){
            throw new InvalidParameterException("Data is not found.");
        }
        // mapper
        StudentResponses.StudentResponse studentResponse = studentMapper.entityToStudentResponse(studentEntity);
        return Optional.ofNullable(studentResponse);
    }

}
