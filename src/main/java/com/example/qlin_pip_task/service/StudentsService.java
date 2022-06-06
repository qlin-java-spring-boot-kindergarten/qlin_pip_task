package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.mapper.StudentMapper;
import com.example.qlin_pip_task.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentsService {

    private final StudentRepository studentRepository;

    private final StudentResponses studentResponses;

    private final StudentMapper studentMapper;

    public StudentResponses getAllStudentsResponse() {

        List<StudentEntity> allStudentsData = studentRepository.findAll();
        ArrayList<StudentResponses.StudentResponse> studentResponsesList = new ArrayList<>();
        for (StudentEntity allStudentsDatum : allStudentsData) {
            StudentResponses.StudentResponse studentResponse = studentMapper.entityToStudentResponse(allStudentsDatum);
            studentResponsesList.add(studentResponse);
        }
        studentResponses.setData(studentResponsesList);
        return studentResponses;
    }

    public Integer save(StudentSubmitRequest studentSubmitRequest){
        StudentEntity studentEntity = studentMapper.requestToStudentEntity(studentSubmitRequest);
        StudentEntity studentData = studentRepository.save(studentEntity);
        return studentData.getId();
    }

    public StudentResponses.StudentResponse getTheStudentResponse(Integer id) {
        Optional<StudentEntity> optionalStudentEntity = studentRepository.findById(id);
        if (optionalStudentEntity.isEmpty()){
            throw new InvalidParameterException("Data is not found.");
        }
        StudentEntity studentEntity = optionalStudentEntity.get();
        return studentMapper.entityToStudentResponse(studentEntity);
    }

}
