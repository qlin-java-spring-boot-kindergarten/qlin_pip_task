package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.StudentResponse;
import com.example.qlin_pip_task.exception.NameInvalidException;
import lombok.RequiredArgsConstructor;
import org.apache.el.stream.Optional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

    public boolean checkIfStudentDataIsValid(StudentResponse.Student student){
        if (student.getName() == null){
            throw new NameInvalidException("Name is invalid.");
        }
        return true;
    }

}
