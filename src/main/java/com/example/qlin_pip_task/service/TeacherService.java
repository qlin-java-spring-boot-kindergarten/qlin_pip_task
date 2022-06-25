package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
import com.example.qlin_pip_task.exception.TeacherNoFoundException;
import com.example.qlin_pip_task.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public void checkIfTeacherIdIsValid(Integer teacherId) {
        if (Objects.isNull(teacherId)) {
            throw new TeacherIdInvalidException("Teacher id is invalid.");
        }
        if (!teacherRepository.existsById(teacherId)) {
            throw new TeacherNoFoundException("Teacher does not exist.");
        }
    }
}
