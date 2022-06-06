package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
@RequiredArgsConstructor
public class ValidationService {

    public void validateStudentData(StudentSubmitRequest studentSubmitRequest) {
        if (studentSubmitRequest.getName() == null || studentSubmitRequest.getName().equals("")) {
            throw new InvalidParameterException("Name is invalid.");
        }
        if (studentSubmitRequest.getGrade() < 1 || studentSubmitRequest.getGrade() > 9) {
            throw new InvalidParameterException("Grade is invalid.");
        }
        if (studentSubmitRequest.getClassroom() < 1 || studentSubmitRequest.getClassroom() > 20) {
            throw new InvalidParameterException("Classroom is invalid.");
        }

    }
}
