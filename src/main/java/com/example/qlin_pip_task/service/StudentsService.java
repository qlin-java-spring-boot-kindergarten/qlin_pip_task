package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.StudentGroupsByHomeworkTypeResponses;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.exception.ClassroomInvalidException;
import com.example.qlin_pip_task.exception.GradeInvalidException;
import com.example.qlin_pip_task.exception.HomeworkAlreadyExistedException;
import com.example.qlin_pip_task.exception.HomeworkContentInvalidException;
import com.example.qlin_pip_task.exception.NameInvalidException;
import com.example.qlin_pip_task.exception.StudentNotFoundException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.mapper.StudentMapper;
import com.example.qlin_pip_task.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
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

    public Integer save(StudentSubmitRequest studentSubmitRequest) {
        StudentEntity studentEntity = studentMapper.requestToStudentEntity(studentSubmitRequest);
        StudentEntity studentData = studentRepository.save(studentEntity);
        return studentData.getId();
    }

    public StudentResponses.StudentResponse getTheStudentResponse(Integer id) {
        Optional<StudentEntity> optionalStudentEntity = studentRepository.findById(id);
        if (optionalStudentEntity.isEmpty()) {
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

    public StudentGroupsByHomeworkTypeResponses getStudentGroupsByHomeworkTypes() {
        List<StudentEntity> allStudentEntitiesList = studentRepository.findAll();
        Set<Integer> allHomeworkTypes = allStudentEntitiesList.stream()
                .map(stduent -> stduent.getHomework().stream()
                        .map(HomeworkEntity::getHomeworkType).collect(Collectors.toSet()))
                .flatMap(Collection::stream).collect(Collectors.toSet());
        TreeSet<Integer> treeSet = new TreeSet<>(allHomeworkTypes);
        StudentGroupsByHomeworkTypeResponses studentGroupsByHomeworkTypeResponses =
                new StudentGroupsByHomeworkTypeResponses(
                        new TreeMap<>(Comparator.comparingInt(s -> Integer.parseInt(s.substring(14)))));
        for (Integer homeworkType : treeSet) {
            List<StudentResponses.StudentResponse> studentEntitiesOfTheHomeworkType = new ArrayList<>();
            for (StudentEntity studentEntity : allStudentEntitiesList) {
                Set<Integer> singleStudentHomeworkTypes = studentEntity.getHomework().stream()
                        .map(HomeworkEntity::getHomeworkType).collect(Collectors.toSet());
                if (singleStudentHomeworkTypes.contains(homeworkType)) {
                    studentEntitiesOfTheHomeworkType.add(studentMapper.entityToStudentResponse(studentEntity));
                }
            }
            List<String> studentNamelist = studentEntitiesOfTheHomeworkType.stream().map(StudentResponses.StudentResponse::getName).collect(Collectors.toList());
            studentGroupsByHomeworkTypeResponses.getHomework().put(String.format("homework_type_%d", homeworkType), studentNamelist);
        }
        return studentGroupsByHomeworkTypeResponses;
    }

    public void updateHomework(Integer id, HomeworkSubmitRequest updateHomeworkSubmitRequest) {
        checkIfTheStudentExisted(id);
        if (updateHomeworkSubmitRequest.getContent() == null || updateHomeworkSubmitRequest.getContent().equals("")) {
            throw new HomeworkContentInvalidException("Homework content is invalid.");
        }
    }


    public void validateStudentData(StudentSubmitRequest studentSubmitRequest) {
        if (studentSubmitRequest.getName() == null || studentSubmitRequest.getName().equals("")) {
            throw new NameInvalidException("Name is invalid.");
        }
        if (studentSubmitRequest.getGrade() < 1 || studentSubmitRequest.getGrade() > 9) {
            throw new GradeInvalidException("Grade is invalid.");
        }
        if (studentSubmitRequest.getClassroom() < 1 || studentSubmitRequest.getClassroom() > 20) {
            throw new ClassroomInvalidException("Classroom is invalid.");
        }

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


