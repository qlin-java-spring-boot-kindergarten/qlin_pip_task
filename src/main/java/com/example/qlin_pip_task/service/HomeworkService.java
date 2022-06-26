package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkAnswerSubmitRequest;
import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentHomeworkIdResponse;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
import com.example.qlin_pip_task.entity.TeacherEntity;
import com.example.qlin_pip_task.exception.ContentInvalidException;
import com.example.qlin_pip_task.exception.DescriptionInvalidException;
import com.example.qlin_pip_task.exception.HomeworkNotFoundException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.repository.HomeworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkMapper homeworkMapper;

    private final HomeworkRepository homeworkRepository;

    private final StudentService studentService;

    private final TeacherService teacherService;

    public HomeworkIdResponse createHomework(HomeworkSubmitRequest homeworkSubmitRequest) {
        Integer teacherId = homeworkSubmitRequest.getTeacherId();
        TeacherEntity teacherEntity = teacherService.getNotNullTeacherEntity(teacherId);
        HomeworkEntity homeworkEntity = homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest);
        homeworkEntity.setTeacherEntity(teacherEntity);
        String description = homeworkSubmitRequest.getDescription();
        checkIfDescriptionIsValid(description);
        HomeworkEntity savedHomeworkEntity = homeworkRepository.save(homeworkEntity);
        return HomeworkIdResponse.builder().id(savedHomeworkEntity.getId()).build();
    }

    public StudentHomeworkIdResponse createStudentHomework(Integer homeworkId, HomeworkAnswerSubmitRequest homeworkAnswerSubmitRequest) {
        HomeworkEntity homeworkEntity = getNotNullHomeworkEntity(homeworkId);
        Integer studentId = homeworkAnswerSubmitRequest.getStudentId();
        StudentEntity studentEntity = studentService.getNotNullStudentEntity(studentId);

        String content = homeworkAnswerSubmitRequest.getContent();
        if (Objects.isNull(content)) {
            throw new ContentInvalidException("Content is null.");
        }
        if (content.isBlank()) {
            throw new ContentInvalidException("Content is empty.");
        }

        studentEntity.setId(studentId);
        Integer classId = studentEntity.getClassId();
        StudentHomeworkEntity studentHomeworkEntity = homeworkMapper.homeworkAnswerRequestToEntity(homeworkAnswerSubmitRequest);
        List<StudentHomeworkEntity> studentHomeworkEntityList = homeworkEntity.getStudentHomework();
        studentHomeworkEntity.setHomeworkEntity(homeworkEntity);
        studentHomeworkEntity.setStudentEntity(studentEntity);
        studentHomeworkEntity.setClassId(classId);
        studentHomeworkEntityList.add(studentHomeworkEntity);
        HomeworkEntity savedHomeworkEntity = homeworkRepository.save(homeworkEntity);
        List<StudentHomeworkEntity> studentHomeworkEntities = savedHomeworkEntity.getStudentHomework();
        Integer id = studentHomeworkEntities.get(studentHomeworkEntities.size() - 1).getId();
        return StudentHomeworkIdResponse.builder().id(id).build();
    }

    private HomeworkEntity getNotNullHomeworkEntity(Integer homeworkId) {
        Optional<HomeworkEntity> optionalHomeworkEntity = homeworkRepository.findById(homeworkId);
        if (optionalHomeworkEntity.isEmpty()) {
            throw new HomeworkNotFoundException("Homework is not found.");
        }
        return optionalHomeworkEntity.get();
    }

    private void checkIfDescriptionIsValid(String description) {
        if (Objects.isNull(description)) {
            throw new DescriptionInvalidException("Description is null");
        }
        if (description.isBlank()) {
            throw new DescriptionInvalidException("Description is empty.");
        }
        if (homeworkRepository.existsByDescription(description)) {
            throw new DescriptionInvalidException("Description is duplicated.");
        }
    }
}
