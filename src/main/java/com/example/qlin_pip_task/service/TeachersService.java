package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.entity.TeacherEntity;
import com.example.qlin_pip_task.exception.ClassIdInvalidException;
import com.example.qlin_pip_task.exception.DescriptionInvalidException;
import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
import com.example.qlin_pip_task.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeachersService {

    private final TeacherRepository teacherRepository;

    public HomeworkIdResponse save(HomeworkSubmitRequest homeworkSubmitRequest) {
        String description = homeworkSubmitRequest.getDescription();
        checkIfHomeworkDescriptionIsValid(description);
        Integer classId = homeworkSubmitRequest.getClassId();
        if (classId == null) {
            throw new ClassIdInvalidException("Class ID cannot be null.");
        }

        Integer teacherId = homeworkSubmitRequest.getTeacherId();
        checkIfTeacherIdIsNull(teacherId);
        Optional<TeacherEntity> optionalTeacherEntity = teacherRepository.findById(teacherId);
        if (optionalTeacherEntity.isEmpty()) {
            throw new TeacherIdInvalidException("Teacher id is not found.");
        }
        return HomeworkIdResponse.builder().build();
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