package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.exception.HomeworkAlreadyExistedException;
import com.example.qlin_pip_task.exception.StudentNotFoundException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.mapper.StudentMapper;
import com.example.qlin_pip_task.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentsService {

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    private final HomeworkMapper homeworkMapper;

    public StudentResponses getAllStudentsResponse() {
        List<StudentEntity> allStudentsData = studentRepository.findAll();

        List<StudentResponses.StudentResponse> studentResponsesList
                = allStudentsData.stream()
                .map(studentMapper::entityToStudentResponse)
                .collect(Collectors.toList());

        return StudentResponses.builder()
                .data(studentResponsesList)
                .build();
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

    public StudentResponses getTheStudentResponseByName(String name) {
        List<StudentEntity> studentEntityList = studentRepository.findAllByName(name);
        List<StudentResponses.StudentResponse> studentResponses = studentEntityList.stream().map(studentMapper::entityToStudentResponse).collect(Collectors.toList());
        return StudentResponses.builder()
                .data(studentResponses)
                .build();
    }

    public Integer submitStudentHomework(Integer id, HomeworkSubmitRequest homeworkSubmitRequest) {
        checkIfTheStudentExisted(id);
        Optional<StudentEntity> optionalStudentEntity = studentRepository.findById(id);
        StudentEntity studentEntity = optionalStudentEntity.get();
        List<HomeworkEntity> theStudentHomeworkList = studentEntity.getHomework();
        checkIfHomeworkAlreadyExisted(theStudentHomeworkList, homeworkSubmitRequest.getHomeworkType());
        HomeworkEntity homeworkEntity = homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest);
        homeworkEntity.setStudentEntity(studentEntity);
        theStudentHomeworkList.add(homeworkEntity);
        StudentEntity theStudentEntity = studentRepository.save(studentEntity);
        List<HomeworkEntity> homeworkEntityList = theStudentEntity.getHomework();
        HomeworkEntity theHomeEntity = homeworkEntityList.get(homeworkEntityList.size() - 1);
        return theHomeEntity.getId();
    }

    private void checkIfTheStudentExisted(Integer id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student not found.");
        }
    }

    private void checkIfHomeworkAlreadyExisted(List<HomeworkEntity> theStudentHomeworkList, Integer requestHomeworkType) {
        for (HomeworkEntity entity : theStudentHomeworkList) {
            if (entity.getHomeworkType().equals(requestHomeworkType)) {
                throw new HomeworkAlreadyExistedException("The homework already existed.");
            }
        }
    }


}

