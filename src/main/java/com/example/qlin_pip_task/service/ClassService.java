package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.entity.ClassEntity;
import com.example.qlin_pip_task.exception.ClassroomInvalidException;
import com.example.qlin_pip_task.exception.GradeInvalidException;
import com.example.qlin_pip_task.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;


    public Integer getClassId(Integer grade, Integer classroom) {
        ClassEntity classEntity = classRepository.findClassEntityByGradeAndClassroom(grade, classroom);
        if (Objects.isNull(classEntity)) {
            ClassEntity newClassEntity = classRepository.save(ClassEntity.builder().grade(grade).classroom(classroom).build());
            return newClassEntity.getId();
        }
        return classEntity.getId();
    }

    public void checkIfGradeIsValid(StudentSubmitRequest studentSubmitRequest) {
        if (studentSubmitRequest.getGrade() < 1 || studentSubmitRequest.getGrade() > 9) {
            throw new GradeInvalidException("Grade is invalid.");
        }
    }

    public void checkIfClassroomIsValid(StudentSubmitRequest studentSubmitRequest) {
        if (studentSubmitRequest.getClassroom() < 1 || studentSubmitRequest.getClassroom() > 20) {
            throw new ClassroomInvalidException("Classroom is invalid.");
        }
    }
}
