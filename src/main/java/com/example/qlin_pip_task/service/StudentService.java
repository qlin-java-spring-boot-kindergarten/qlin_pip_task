package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.StudentHomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentGroupsByHomeworkTypeResponses;
import com.example.qlin_pip_task.dto.response.StudentIdResponse;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.ClassEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
import com.example.qlin_pip_task.exception.HomeworkAlreadyExistedException;
import com.example.qlin_pip_task.exception.HomeworkContentInvalidException;
import com.example.qlin_pip_task.exception.HomeworkTypeNotExistsException;
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
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClassService classService;
    private final StudentMapper studentMapper;
    private final HomeworkMapper homeworkMapper;

    public StudentResponses getStudents() {
        List<StudentEntity> studentEntityList = studentRepository.findAll();
        List<StudentResponses.StudentResponse> studentResponses = getStudentResponseList(studentEntityList);
        return StudentResponses.builder().data(studentResponses).build();
    }

    public StudentIdResponse createStudent(StudentSubmitRequest studentSubmitRequest) {
        Integer classId = classService.getClassId(studentSubmitRequest.getGrade(), studentSubmitRequest.getClassroom());
        StudentEntity studentEntity = StudentEntity.builder().name(studentSubmitRequest.getName()).classId(classId).build();
        StudentEntity newStudentEntity = studentRepository.save(studentEntity);
        return StudentIdResponse.builder().id(newStudentEntity.getId()).build();
    }

    public StudentResponses.StudentResponse getStudentById(Integer id) {
        StudentEntity studentEntity = getNotNullStudentEntity(id);
        Integer classId = studentEntity.getClassId();
        ClassEntity classEntity = classService.getClassEntityById(classId);
        return studentMapper.entityToStudentResponse(studentEntity, classEntity);
    }

    public StudentResponses getStudentByName(Map<String, String> queryMap) {
        List<StudentEntity> studentEntityList = studentRepository.findAllByName(queryMap.get("name"));
        List<StudentResponses.StudentResponse> studentResponses = getStudentResponseList(studentEntityList);
        return StudentResponses.builder().data(studentResponses).build();
    }

    public HomeworkIdResponse createStudentHomework(Integer id, StudentHomeworkSubmitRequest studentHomeworkSubmitRequest) {
        StudentEntity studentEntity = getNotNullStudentEntity(id);
        List<StudentHomeworkEntity> theStudentHomeworkList = studentEntity.getStudentHomework();
        checkIfHomeworkAlreadyExisted(theStudentHomeworkList, studentHomeworkSubmitRequest.getHomeworkId());
        StudentHomeworkEntity studentHomeworkEntity = homeworkMapper.studentHomeworkRequestToEntity(studentHomeworkSubmitRequest);
        studentHomeworkEntity.setStudentEntity(studentEntity);
        theStudentHomeworkList.add(studentHomeworkEntity);
        StudentEntity theStudentEntity = studentRepository.save(studentEntity);
        List<StudentHomeworkEntity> studentHomeworkEntityList = theStudentEntity.getStudentHomework();
        StudentHomeworkEntity theHomeEntity = studentHomeworkEntityList.get(studentHomeworkEntityList.size() - 1);
        return homeworkMapper.homeworkEntityToHomeworkIdResponse(theHomeEntity);
    }

    public StudentEntity getNotNullStudentEntity(Integer studentId) {
        Optional<StudentEntity> optionalStudentEntity = studentRepository.findById(studentId);
        if (optionalStudentEntity.isEmpty()) {
            throw new StudentNotFoundException("Student is not found.");
        }
        return optionalStudentEntity.get();
    }

    public StudentGroupsByHomeworkTypeResponses getStudentGroupsByHomework() {
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
            Set<Integer> singleStudentHomeworkTypes = studentEntity.getStudentHomework().stream()
                    .map(studentHomeworkEntity -> studentHomeworkEntity.getHomeworkEntity().getId()).collect(Collectors.toSet());
            if (singleStudentHomeworkTypes.contains(homeworkType)) {
                Integer classId = studentEntity.getClassId();
                studentEntitiesOfTheHomeworkType.add(studentMapper.entityToStudentResponse(studentEntity, classService.getClassEntityById(classId)));
            }
        }
    }

    public void updateStudentHomework(Integer id, StudentHomeworkSubmitRequest updateStudentHomeworkSubmitRequest) {
        StudentEntity studentEntity = getNotNullStudentEntity(id);
        List<StudentEntity> allStudentEntitiesList = studentRepository.findAll();
        checkIfTheHomeworkSubmitRequestIsValid(updateStudentHomeworkSubmitRequest, allStudentEntitiesList);
        List<StudentHomeworkEntity> homeworkList = studentEntity.getStudentHomework();
        List<StudentHomeworkEntity> list = homeworkList.stream().filter(studentHomeworkEntity -> studentHomeworkEntity.getHomeworkEntity().getId()
                .equals(updateStudentHomeworkSubmitRequest.getHomeworkId())).collect(Collectors.toList());
        StudentHomeworkEntity studentHomeworkEntity = list.get(0);
        studentHomeworkEntity.setContent(updateStudentHomeworkSubmitRequest.getContent());
        studentHomeworkEntity.setStudentEntity(studentEntity);
        studentRepository.save(studentEntity);
    }

    private void checkIfTheHomeworkSubmitRequestIsValid(StudentHomeworkSubmitRequest updateStudentHomeworkSubmitRequest, List<StudentEntity> allStudentEntitiesList) {
        checkIfHomeworkContentIsValid(updateStudentHomeworkSubmitRequest);
        checkIfHomeworkTypeIsExisted(updateStudentHomeworkSubmitRequest, allStudentEntitiesList);
    }

    private void checkIfHomeworkTypeIsExisted(StudentHomeworkSubmitRequest updateStudentHomeworkSubmitRequest, List<StudentEntity> allStudentEntitiesList) {
        if (!getAllHomeworkTypes(allStudentEntitiesList).contains(updateStudentHomeworkSubmitRequest.getHomeworkId())) {
            throw new HomeworkTypeNotExistsException("Homework type is in invalid.");
        }
    }

    private void checkIfHomeworkContentIsValid(StudentHomeworkSubmitRequest updateStudentHomeworkSubmitRequest) {
        if (updateStudentHomeworkSubmitRequest.getContent() == null || updateStudentHomeworkSubmitRequest.getContent().equals("")) {
            throw new HomeworkContentInvalidException("Homework content is invalid.");
        }
    }

    public void validateStudentData(StudentSubmitRequest studentSubmitRequest) {
        if (studentSubmitRequest.getName() == null || studentSubmitRequest.getName().equals("")) {
            throw new NameInvalidException("Name is invalid.");
        }
        classService.checkIfGradeIsValid(studentSubmitRequest.getGrade());
        classService.checkIfClassroomIsValid(studentSubmitRequest.getClassroom());
    }

    private void checkIfHomeworkAlreadyExisted(List<StudentHomeworkEntity> theStudentHomeworkList, Integer requestHomeworkType) {
        for (StudentHomeworkEntity entity : theStudentHomeworkList) {
            if (entity.getHomeworkEntity().getId().equals(requestHomeworkType)) {
                throw new HomeworkAlreadyExistedException("The homework already existed.");
            }
        }
    }

    private TreeSet<Integer> getAllHomeworkTypes(List<StudentEntity> allStudentEntitiesList) {
        return allStudentEntitiesList.stream()
                .map(student -> student.getStudentHomework().stream()
                        .map(studentHomeworkEntity -> studentHomeworkEntity.getHomeworkEntity().getId()).collect(Collectors.toSet()))
                .flatMap(Collection::stream).collect(Collectors.toCollection(TreeSet::new));
    }

    private List<StudentResponses.StudentResponse> getStudentResponseList(List<StudentEntity> studentEntityList) {
        List<Integer> classIdsList = studentEntityList.stream().map(StudentEntity::getClassId).collect(Collectors.toList());
        List<ClassEntity> classEntityList = classIdsList.stream().map(classService::getClassEntityById).collect(Collectors.toList());
        return IntStream.range(0, studentEntityList.size())
                .mapToObj(i -> studentMapper.entityToStudentResponse(studentEntityList.get(i), classEntityList.get(i)))
                .collect(Collectors.toList());
    }
}


