package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.StudentResponse;
import com.example.qlin_pip_task.exception.GradeInvalidException;
import com.example.qlin_pip_task.exception.NameInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

    public boolean checkIfStudentDataIsValid(StudentResponse.Student student) {
        if (student.getName() == null) {
            throw new NameInvalidException("Name is invalid.");
        }
        if (student.getGrade() < 1 || student.getGrade() > 9) {
            throw new GradeInvalidException("Grade is invalid.");
        }
        return true;
    }

}
