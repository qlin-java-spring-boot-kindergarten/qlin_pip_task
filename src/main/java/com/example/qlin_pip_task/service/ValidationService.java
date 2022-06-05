package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.StudentResponse;
import com.example.qlin_pip_task.exception.ClassroomInvalidException;
import com.example.qlin_pip_task.exception.DataNotFoundException;
import com.example.qlin_pip_task.exception.GradeInvalidException;
import com.example.qlin_pip_task.exception.NameInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final StudentsService studentsService;

    public void checkIfStudentDataIsValid(StudentResponse.Student student) {
        if (student.getName() == null || student.getName().equals("")) {
            throw new NameInvalidException("Name is invalid.");
        }
        if (student.getGrade() < 1 || student.getGrade() > 9) {
            throw new GradeInvalidException("Grade is invalid.");
        }
        if (student.getClassroom() < 1 || student.getClassroom() > 20) {
            throw new ClassroomInvalidException("Classroom is invalid.");
        }
    }


    public Optional<StudentResponse.Student> getTheExistedStudentData(Integer id) {
        Optional<StudentResponse.Student> theStudentData = studentsService.getTheStudentResponse(id);
        if(theStudentData.isEmpty()) {
            throw new DataNotFoundException("Data is not found.");
        }
        return theStudentData;
    }
}
