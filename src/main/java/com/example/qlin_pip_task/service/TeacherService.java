package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.entity.TeacherEntity;
import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
import com.example.qlin_pip_task.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public void checkIfTeacherEntityExists(Integer teacherId) {
        Optional<TeacherEntity> optionalTeacherEntity = teacherRepository.findById(teacherId);
        if (optionalTeacherEntity.isEmpty()) {
            throw new TeacherIdInvalidException("Teacher is not found.");
        }
    }
}
