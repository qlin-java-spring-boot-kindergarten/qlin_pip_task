package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.NewStudentHomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentHomeworkIdResponse;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
import com.example.qlin_pip_task.entity.TeacherEntity;
import com.example.qlin_pip_task.exception.ClassIdInvalidException;
import com.example.qlin_pip_task.exception.DescriptionInvalidException;
import com.example.qlin_pip_task.exception.HomeworkIdInvalidException;
import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.mapper.NewStudentHomeworkMapper;
import com.example.qlin_pip_task.repository.HomeworkRepository;
import com.example.qlin_pip_task.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final HomeworkMapper homeworkMapper;
    private final TeacherRepository teacherRepository;
    private final StudentsService studentsService;
    private final ClassService classService;
    private final NewStudentHomeworkMapper newStudentHomeworkMapper;

    public HomeworkIdResponse save(HomeworkSubmitRequest homeworkSubmitRequest) {
        String description = homeworkSubmitRequest.getDescription();
        checkIfHomeworkDescriptionIsValid(description);
        Integer classId = homeworkSubmitRequest.getClassId();
        classService.checkIfClassIdIsValid(classId);
        Integer teacherId = homeworkSubmitRequest.getTeacherId();
        checkIfTeacherIdIsNull(teacherId);
        Optional<TeacherEntity> optionalTeacherEntity = teacherRepository.findById(teacherId);
        if (optionalTeacherEntity.isEmpty()) {
            throw new TeacherIdInvalidException("Teacher id is not found.");
        }
        HomeworkEntity homeworkEntity = homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest);
        HomeworkEntity savedNewHomework = homeworkRepository.save(homeworkEntity);
        return HomeworkIdResponse.builder().id(savedNewHomework.getId()).build();
    }

    public StudentHomeworkIdResponse getStudentHomeworkIdResponse(Integer homeworkId, NewStudentHomeworkSubmitRequest newStudentHomeworkSubmitRequest) {
        Optional<HomeworkEntity> optionalHomeworkEntity = homeworkRepository.findById(homeworkId);
        if (optionalHomeworkEntity.isEmpty()) {
            throw new HomeworkIdInvalidException("The homework id is not found.");
        }
        Integer studentId = newStudentHomeworkSubmitRequest.getStudentId();
        studentsService.checkIfStudentIdIsNull(studentId);
        StudentHomeworkEntity studentHomeworkEntity =
                newStudentHomeworkMapper.homeworkRequestToEntity(newStudentHomeworkSubmitRequest);
        Integer classId = newStudentHomeworkSubmitRequest.getClassId();
        classService.checkIfClassIdIsValid(classId);
        if (!studentHomeworkEntity.getStudentEntity().getClassId().equals(classId)) {
            throw new ClassIdInvalidException("The class id does not match the student id provided.");
        }
        return StudentHomeworkIdResponse.builder().id(999).build();
    }

    private void checkIfTeacherIdIsNull(Integer teacherId) {
        if (teacherId == null) {
            throw new TeacherIdInvalidException("Teacher id is invalid.");
        }
    }

    private void checkIfHomeworkDescriptionIsValid(String description) {
        if (description == null) {
            throw new DescriptionInvalidException("Description cannot be null.");
        }
        if (description.isBlank()) {
            throw new DescriptionInvalidException("Description cannot be empty.");
        }
    }
}
