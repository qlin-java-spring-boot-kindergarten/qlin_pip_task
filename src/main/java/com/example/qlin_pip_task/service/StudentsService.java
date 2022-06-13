package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentGroupsByHomeworkTypeResponses;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.dto.response.StudentSavedIdResponse;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.exception.ClassroomInvalidException;
import com.example.qlin_pip_task.exception.GradeInvalidException;
import com.example.qlin_pip_task.exception.HomeworkAlreadyExistedException;
import com.example.qlin_pip_task.exception.HomeworkContentInvalidException;
import com.example.qlin_pip_task.exception.HomeworkTypeNotExistedException;
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
import java.util.Map;
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
        return StudentResponses.builder().data(studentResponsesList).build();
    }

    public StudentSavedIdResponse save(StudentSubmitRequest studentSubmitRequest) {
        StudentEntity studentEntity = studentMapper.requestToStudentEntity(studentSubmitRequest);
        StudentEntity studentData = studentRepository.save(studentEntity);
        return StudentSavedIdResponse.builder().id(studentData.getId()).build();
    }

    public StudentResponses.StudentResponse getTheStudentResponse(Integer id) {
        Optional<StudentEntity> optionalStudentEntity = studentRepository.findById(id);
        if (optionalStudentEntity.isEmpty()) {
            throw new InvalidParameterException("Data is not found.");
        }
        StudentEntity studentEntity = optionalStudentEntity.get();
        return studentMapper.entityToStudentResponse(studentEntity);
    }

    public StudentResponses getTheStudentResponseByName(Map<String, String> queryMap) {
        List<StudentEntity> studentEntityList = studentRepository.findAllByName(queryMap.get("name"));
        List<StudentResponses.StudentResponse> studentResponses =
                studentEntityList.stream().map(studentMapper::entityToStudentResponse).collect(Collectors.toList());
        return StudentResponses.builder().data(studentResponses).build();
    }

    public HomeworkIdResponse submitStudentHomework(Integer id, HomeworkSubmitRequest homeworkSubmitRequest) {
        StudentEntity studentEntity = getNotNullableStudentEntity(id);
        List<HomeworkEntity> theStudentHomeworkList = studentEntity.getHomework();
        checkIfHomeworkAlreadyExisted(theStudentHomeworkList, homeworkSubmitRequest.getHomeworkType());
        HomeworkEntity homeworkEntity = homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest);
        homeworkEntity.setStudentEntity(studentEntity);
        theStudentHomeworkList.add(homeworkEntity);
        StudentEntity theStudentEntity = studentRepository.save(studentEntity);
        List<HomeworkEntity> homeworkEntityList = theStudentEntity.getHomework();
        HomeworkEntity theHomeEntity = homeworkEntityList.get(homeworkEntityList.size() - 1);
        return homeworkMapper.homeworkEntityToHomeworkIdResponse(theHomeEntity);
    }

    private StudentEntity getNotNullableStudentEntity(Integer id) {
        Optional<StudentEntity> optionalStudentEntity = studentRepository.findById(id);
        if (optionalStudentEntity.isEmpty()) {
            throw new StudentNotFoundException("Student not found.");
        }
        return optionalStudentEntity.get();
    }

    public StudentGroupsByHomeworkTypeResponses getStudentGroupsByHomeworkTypes() {
        List<StudentEntity> allStudentEntitiesList = studentRepository.findAll();
        TreeSet<Integer> allHomeworkTypes = getAllHomeworkTypes(allStudentEntitiesList);
        StudentGroupsByHomeworkTypeResponses studentGroupsByHomeworkTypeResponses =
                new StudentGroupsByHomeworkTypeResponses(
                        new TreeMap<>(Comparator.comparingInt(s -> Integer.parseInt(s.substring(14)))));
        for (Integer homeworkType : allHomeworkTypes) {
            List<StudentResponses.StudentResponse> studentEntitiesOfTheHomeworkType = new ArrayList<>();
            getStudentsOfTheHomeworkType(allStudentEntitiesList, homeworkType, studentEntitiesOfTheHomeworkType);
            List<String> studentNameList = studentEntitiesOfTheHomeworkType.stream().map(StudentResponses.StudentResponse::getName).collect(Collectors.toList());
            studentGroupsByHomeworkTypeResponses.getHomework().put(String.format("homework_type_%d", homeworkType), studentNameList);
        }
        return studentGroupsByHomeworkTypeResponses;
    }

    private void getStudentsOfTheHomeworkType(List<StudentEntity> allStudentEntitiesList, Integer homeworkType, List<StudentResponses.StudentResponse> studentEntitiesOfTheHomeworkType) {
        for (StudentEntity studentEntity : allStudentEntitiesList) {
            Set<Integer> singleStudentHomeworkTypes = studentEntity.getHomework().stream()
                    .map(HomeworkEntity::getHomeworkType).collect(Collectors.toSet());
            if (singleStudentHomeworkTypes.contains(homeworkType)) {
                studentEntitiesOfTheHomeworkType.add(studentMapper.entityToStudentResponse(studentEntity));
            }
        }
    }

    public void updateHomework(Integer id, HomeworkSubmitRequest updateHomeworkSubmitRequest) {
        StudentEntity studentEntity = getNotNullableStudentEntity(id);
        List<StudentEntity> allStudentEntitiesList = studentRepository.findAll();
        checkIfTheHomeworkSubmitRequestIsValid(updateHomeworkSubmitRequest, allStudentEntitiesList);
        List<HomeworkEntity> homeworkList = studentEntity.getHomework();
        List<HomeworkEntity> list = homeworkList.stream().filter(homeworkEntity -> homeworkEntity.getHomeworkType()
                .equals(updateHomeworkSubmitRequest.getHomeworkType())).collect(Collectors.toList());
        HomeworkEntity homeworkEntity = list.get(0);
        homeworkEntity.setContent(updateHomeworkSubmitRequest.getContent());
        homeworkEntity.setStudentEntity(studentEntity);
        studentRepository.save(studentEntity);
    }

    private void checkIfTheHomeworkSubmitRequestIsValid(HomeworkSubmitRequest updateHomeworkSubmitRequest, List<StudentEntity> allStudentEntitiesList) {
        checkIfHomeworkContentIsValid(updateHomeworkSubmitRequest);
        checkIfHomeworkTypeIsExisted(updateHomeworkSubmitRequest, allStudentEntitiesList);
    }

    private void checkIfHomeworkTypeIsExisted(HomeworkSubmitRequest updateHomeworkSubmitRequest, List<StudentEntity> allStudentEntitiesList) {
        if (!getAllHomeworkTypes(allStudentEntitiesList).contains(updateHomeworkSubmitRequest.getHomeworkType())){
            throw new HomeworkTypeNotExistedException("Homework type is in invalid.");
        }
    }

    private void checkIfHomeworkContentIsValid(HomeworkSubmitRequest updateHomeworkSubmitRequest) {
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

    private void checkIfHomeworkAlreadyExisted(List<HomeworkEntity> theStudentHomeworkList, Integer requestHomeworkType) {
        for (HomeworkEntity entity : theStudentHomeworkList) {
            if (entity.getHomeworkType().equals(requestHomeworkType)) {
                throw new HomeworkAlreadyExistedException("The homework already existed.");
            }
        }
    }

    private TreeSet<Integer> getAllHomeworkTypes(List<StudentEntity> allStudentEntitiesList) {
        return allStudentEntitiesList.stream()
                .map(stduent -> stduent.getHomework().stream()
                        .map(HomeworkEntity::getHomeworkType).collect(Collectors.toSet()))
                .flatMap(Collection::stream).collect(Collectors.toCollection(TreeSet::new));
    }
}


