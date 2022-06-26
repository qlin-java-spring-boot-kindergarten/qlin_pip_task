package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.entity.TeacherEntity;
import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
import com.example.qlin_pip_task.exception.TeacherNoFoundException;
import com.example.qlin_pip_task.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherEntity getNotNullTeacherEntity(Integer teacherId) {
        if (Objects.isNull(teacherId)) {
            throw new TeacherIdInvalidException("Teacher id is invalid.");
        }
        Optional<TeacherEntity> optionalTeacherEntity = teacherRepository.findById(teacherId);
        if (optionalTeacherEntity.isEmpty()) {
            throw new TeacherNoFoundException("Teacher does not exist.");
        }
        return optionalTeacherEntity.get();
    }
}
