package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TeacherService {

    public void checkIfTeacherIdIsValid(Integer teacherId) {
        if (Objects.isNull(teacherId)) {
            throw new TeacherIdInvalidException("Teacher id is invalid.");
        }
    }
}
