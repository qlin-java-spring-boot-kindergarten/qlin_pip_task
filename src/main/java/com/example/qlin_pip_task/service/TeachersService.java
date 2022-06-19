package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
import com.example.qlin_pip_task.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeachersService {

    private final TeacherRepository teacherRepository;

    public HomeworkIdResponse save(HomeworkSubmitRequest homeworkSubmitRequest) {
        Integer teacherId = homeworkSubmitRequest.getTeacherId();
        if (teacherId == null) {
            throw new TeacherIdInvalidException("Teacher id is invalid.");
        }
        return HomeworkIdResponse.builder().build();
    }
}
