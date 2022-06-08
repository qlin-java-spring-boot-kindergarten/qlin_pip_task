package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.exception.ClassroomInvalidException;
import com.example.qlin_pip_task.exception.GradeInvalidException;
import com.example.qlin_pip_task.exception.NameInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

    public void validateStudentData(StudentSubmitRequest studentSubmitRequest) {
        if (studentSubmitRequest.getName() == null || studentSubmitRequest.getName().equals("")) {
            throw new NameInvalidException("Name is invalid.");
        }
        if (studentSubmitRequest.getGrade() < 1 || studentSubmitRequest.getGrade() > 9) {
            throw new GradeInvalidException("Grade is invalid.");
        }
        if (studentSubmitRequest.getClassroom() < 1 || studentSubmitRequest.getClassroom() > 20) {
            throw new ClassroomInvalidException("Classroom is invalid.");
        }

    }
}
