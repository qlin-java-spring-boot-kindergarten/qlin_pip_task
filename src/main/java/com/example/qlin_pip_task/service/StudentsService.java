package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.exception.StudentNotFoundException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.mapper.StudentMapper;
import com.example.qlin_pip_task.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
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

    public String submitStudentHomework(Integer id, HomeworkSubmitRequest homeworkSubmitRequest) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student not found.");
        }
        HomeworkEntity homeworkEntity = homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest);
        Optional<StudentEntity> optionalStudentEntity = studentRepository.findById(id);
        StudentEntity studentEntity = optionalStudentEntity.get();

        homeworkEntity.setStudentEntity(studentEntity);

        studentEntity.getHomework().add(homeworkEntity);
        StudentEntity save = studentRepository.save(studentEntity);

        List<HomeworkEntity> homeworkEntityList = save.getHomework();
        HomeworkEntity homeworkEntity1 = homeworkEntityList.get(homeworkEntityList.size() - 1);
        return homeworkEntity1.getId().toString();
    }
}
