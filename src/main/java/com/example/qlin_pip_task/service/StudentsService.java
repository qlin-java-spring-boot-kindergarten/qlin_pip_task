package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentGroupsByHomeworkTypeResponses;
import com.example.qlin_pip_task.dto.response.StudentIdResponse;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
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
    private final ClassService classService;
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

    public StudentIdResponse save(StudentSubmitRequest studentSubmitRequest) {

        return StudentIdResponse.builder().build();
    }

    public StudentResponses.StudentResponse getTheStudentResponse(Integer id) {
        StudentEntity studentEntity = getNotNullableStudentEntity(id);
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
        List<StudentHomeworkEntity> theStudentHomeworkList = studentEntity.getHomework();
        checkIfHomeworkAlreadyExisted(theStudentHomeworkList, homeworkSubmitRequest.getHomeworkId());
        StudentHomeworkEntity studentHomeworkEntity = homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest);
        studentHomeworkEntity.setStudentEntity(studentEntity);
        theStudentHomeworkList.add(studentHomeworkEntity);
        StudentEntity theStudentEntity = studentRepository.save(studentEntity);
        List<StudentHomeworkEntity> studentHomeworkEntityList = theStudentEntity.getHomework();
        StudentHomeworkEntity theHomeEntity = studentHomeworkEntityList.get(studentHomeworkEntityList.size() - 1);
        return homeworkMapper.homeworkEntityToHomeworkIdResponse(theHomeEntity);
    }

    private StudentEntity getNotNullableStudentEntity(Integer id) {
        Optional<StudentEntity> optionalStudentEntity = studentRepository.findById(id);
        if (optionalStudentEntity.isEmpty()) {
            throw new StudentNotFoundException("Student is not found.");
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
                    .map(StudentHomeworkEntity::getHomeworkId).collect(Collectors.toSet());
            if (singleStudentHomeworkTypes.contains(homeworkType)) {
                studentEntitiesOfTheHomeworkType.add(studentMapper.entityToStudentResponse(studentEntity));
            }
        }
    }

    public void updateHomework(Integer id, HomeworkSubmitRequest updateHomeworkSubmitRequest) {
        StudentEntity studentEntity = getNotNullableStudentEntity(id);
        List<StudentEntity> allStudentEntitiesList = studentRepository.findAll();
        checkIfTheHomeworkSubmitRequestIsValid(updateHomeworkSubmitRequest, allStudentEntitiesList);
        List<StudentHomeworkEntity> homeworkList = studentEntity.getHomework();
        List<StudentHomeworkEntity> list = homeworkList.stream().filter(homeworkEntity -> homeworkEntity.getHomeworkId()
                .equals(updateHomeworkSubmitRequest.getHomeworkId())).collect(Collectors.toList());
        StudentHomeworkEntity studentHomeworkEntity = list.get(0);
        studentHomeworkEntity.setContent(updateHomeworkSubmitRequest.getContent());
        studentHomeworkEntity.setStudentEntity(studentEntity);
        studentRepository.save(studentEntity);
    }

    private void checkIfTheHomeworkSubmitRequestIsValid(HomeworkSubmitRequest updateHomeworkSubmitRequest, List<StudentEntity> allStudentEntitiesList) {
        checkIfHomeworkContentIsValid(updateHomeworkSubmitRequest);
        checkIfHomeworkTypeIsExisted(updateHomeworkSubmitRequest, allStudentEntitiesList);
    }

    private void checkIfHomeworkTypeIsExisted(HomeworkSubmitRequest updateHomeworkSubmitRequest, List<StudentEntity> allStudentEntitiesList) {
        if (!getAllHomeworkTypes(allStudentEntitiesList).contains(updateHomeworkSubmitRequest.getHomeworkId())){
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
        classService.checkIfGradeIsValid(studentSubmitRequest);
        classService.checkIfClassroomIsValid(studentSubmitRequest);
    }

    private void checkIfHomeworkAlreadyExisted(List<StudentHomeworkEntity> theStudentHomeworkList, Integer requestHomeworkType) {
        for (StudentHomeworkEntity entity : theStudentHomeworkList) {
            if (entity.getHomeworkId().equals(requestHomeworkType)) {
                throw new HomeworkAlreadyExistedException("The homework already existed.");
            }
        }
    }

    private TreeSet<Integer> getAllHomeworkTypes(List<StudentEntity> allStudentEntitiesList) {
        return allStudentEntitiesList.stream()
                .map(student -> student.getHomework().stream()
                        .map(StudentHomeworkEntity::getHomeworkId).collect(Collectors.toSet()))
                .flatMap(Collection::stream).collect(Collectors.toCollection(TreeSet::new));
    }
}


