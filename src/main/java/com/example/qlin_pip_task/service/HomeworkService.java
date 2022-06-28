package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkAnswerSubmitRequest;
import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentHomeworkGroupByIdAndDateAndClassResponses;
import com.example.qlin_pip_task.dto.response.StudentHomeworkIdResponse;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
import com.example.qlin_pip_task.entity.TeacherEntity;
import com.example.qlin_pip_task.exception.ContentInvalidException;
import com.example.qlin_pip_task.exception.DateInvalidException;
import com.example.qlin_pip_task.exception.HomeworkIdInvalidException;
import com.example.qlin_pip_task.exception.HomeworkNotFoundException;
import com.example.qlin_pip_task.exception.StudentHomeworkAlreadyExistedException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.repository.HomeworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    public static final String GRADE = "grade";
    public static final String CLASSROOM = "classroom";
    public static final String CREATED_AT = "created_at";
    public static final String HOMEWORK_ID = "homework_id";
    private final HomeworkMapper homeworkMapper;

    private final HomeworkRepository homeworkRepository;

    private final StudentService studentService;

    private final ClassService classService;

    private final TeacherService teacherService;

    public HomeworkIdResponse createHomework(HomeworkSubmitRequest homeworkSubmitRequest) {
        Integer teacherId = homeworkSubmitRequest.getTeacherId();
        TeacherEntity teacherEntity = teacherService.getNotNullTeacherEntity(teacherId);
        HomeworkEntity homeworkEntity = homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest);
        homeworkEntity.setTeacherEntity(teacherEntity);
        String content = homeworkSubmitRequest.getContent();
        checkIfContentIsValid(content);
        HomeworkEntity savedHomeworkEntity = homeworkRepository.save(homeworkEntity);
        return HomeworkIdResponse.builder().id(savedHomeworkEntity.getId()).build();
    }

    public StudentHomeworkIdResponse createStudentHomework(Integer homeworkId, HomeworkAnswerSubmitRequest homeworkAnswerSubmitRequest) {
        HomeworkEntity homeworkEntity = getNotNullHomeworkEntity(homeworkId);
        List<StudentHomeworkEntity> studentHomeworkEntityList = homeworkEntity.getStudentHomework();
        Integer studentId = homeworkAnswerSubmitRequest.getStudentId();
        String content = homeworkAnswerSubmitRequest.getContent();
        checkIfContentIsValid(content);
        checkIfStudentHomeworkIsDuplicated(studentHomeworkEntityList, homeworkId, studentId);
        StudentEntity studentEntity = studentService.getNotNullStudentEntity(studentId);
        studentEntity.setId(studentId);
        Integer classId = studentEntity.getClassId();
        StudentHomeworkEntity studentHomeworkEntity = homeworkMapper.homeworkAnswerRequestToEntity(homeworkAnswerSubmitRequest);
        studentHomeworkEntity.setHomeworkEntity(homeworkEntity);
        studentHomeworkEntity.setStudentEntity(studentEntity);
        studentHomeworkEntity.setClassId(classId);
        studentHomeworkEntityList.add(studentHomeworkEntity);
        HomeworkEntity savedHomeworkEntity = homeworkRepository.save(homeworkEntity);
        List<StudentHomeworkEntity> studentHomeworkEntities = savedHomeworkEntity.getStudentHomework();
        Integer id = studentHomeworkEntities.get(studentHomeworkEntities.size() - 1).getId();
        return StudentHomeworkIdResponse.builder().id(id).build();
    }

    public StudentHomeworkGroupByIdAndDateAndClassResponses getStudentHomeworkByHomeworkIdAndClassIdAndDate(Map<String, String> queryMap) {
        String gradeStr = queryMap.get(GRADE);
        String classroomStr = queryMap.get(CLASSROOM);
        Integer classId = classService.getValidClassId(gradeStr, classroomStr);
        String dateStr = queryMap.get(CREATED_AT);
        LocalDate date = getValidLocalDate(dateStr);
        String homeworkIdStr = queryMap.get(HOMEWORK_ID);
        HomeworkEntity homeworkEntity = getValidHomeworkEntity(homeworkIdStr);
        List<StudentHomeworkEntity> studentHomeworkEntityList = homeworkEntity.getStudentHomework();
        List<StudentHomeworkEntity> list = studentHomeworkEntityList.stream().filter(studentHomeworkEntity ->
                studentHomeworkEntity.getClassId().equals(classId)
                        && studentHomeworkEntity.getCreatedAt().equals(date)).collect(Collectors.toList());
        List<StudentHomeworkGroupByIdAndDateAndClassResponses.StudentHomeworkResponse> responses = new ArrayList<>();
        list.forEach(studentHomeworkEntity -> {
            StudentHomeworkGroupByIdAndDateAndClassResponses.StudentHomeworkResponse studentHomeworkResponse =
                    homeworkMapper.homeworkEntityToGroupedStudentHomeworkResponse(studentHomeworkEntity);
            studentHomeworkResponse.setStudentId(studentHomeworkEntity.getStudentEntity().getId());
            responses.add(studentHomeworkResponse);
        });

        return StudentHomeworkGroupByIdAndDateAndClassResponses.builder()
                .homeworkId(homeworkIdStr)
                .grade(gradeStr)
                .classroom(classroomStr)
                .createdAt(dateStr)
                .studentHomeworkList(responses)
                .build();
    }

    private HomeworkEntity getValidHomeworkEntity(String homeworkIdStr) {
        checkIfHomeworkStrIsValid(homeworkIdStr);
        Integer homeworkId = Integer.valueOf(homeworkIdStr);
        return getNotNullHomeworkEntity(homeworkId);
    }

    private void checkIfHomeworkStrIsValid(String homeworkIdStr) {
        if (Objects.isNull(homeworkIdStr)) {
            throw new HomeworkIdInvalidException("Homework id is invalid.");
        }
        try {
            Integer.parseInt(homeworkIdStr);
        } catch (NumberFormatException e) {
            throw new HomeworkIdInvalidException("Homework id is invalid.");
        }
    }

    private LocalDate getValidLocalDate(String dateStr) {
        checkIfDateStrIsValid(dateStr);
        LocalDate date = LocalDate.parse(dateStr);
        checkIfDateExists(date);
        return date;
    }

    private void checkIfDateExists(LocalDate date) {
        if (!homeworkRepository.existsByStudentHomeworkCreatedAt(date)) {
            throw new DateInvalidException("Date is invalid.");
        }
    }

    private void checkIfDateStrIsValid(String dateStr) {
        if (Objects.isNull(dateStr)) {
            throw new DateInvalidException("Date is null.");
        }
        try {
            LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new DateInvalidException("Date is invalid.");
        }
    }

    private void checkIfStudentHomeworkIsDuplicated(List<StudentHomeworkEntity> studentHomeworkEntityList, Integer homeworkId, Integer studentId) {
        for (StudentHomeworkEntity studentHomeworkEntity : studentHomeworkEntityList) {
            if (studentHomeworkEntity.getHomeworkEntity().getId().equals(homeworkId)
                    && studentHomeworkEntity.getStudentEntity().getId().equals(studentId)) {
                throw new StudentHomeworkAlreadyExistedException("Student homework is existed.");
            }
        }
    }

    private HomeworkEntity getNotNullHomeworkEntity(Integer homeworkId) {
        Optional<HomeworkEntity> optionalHomeworkEntity = homeworkRepository.findById(homeworkId);
        if (optionalHomeworkEntity.isEmpty()) {
            throw new HomeworkNotFoundException("Homework is not found.");
        }
        return optionalHomeworkEntity.get();
    }

    private void checkIfContentIsValid(String content) {
        if (Objects.isNull(content)) {
            throw new ContentInvalidException("Content is null.");
        }
        if (content.isBlank()) {
            throw new ContentInvalidException("Content is empty.");
        }
        if (homeworkRepository.existsByContent(content)) {
            throw new ContentInvalidException("Content is duplicated.");
        }
    }
}
