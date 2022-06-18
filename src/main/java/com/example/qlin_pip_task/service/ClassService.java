package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.entity.ClassEntity;
import com.example.qlin_pip_task.exception.ClassNotExistsException;
import com.example.qlin_pip_task.exception.ClassroomInvalidException;
import com.example.qlin_pip_task.exception.GradeInvalidException;
import com.example.qlin_pip_task.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;


    public Integer getClassId(Integer grade, Integer classroom) {
        checkIfGradeIsValid(grade);
        checkIfClassroomIsValid(classroom);
        Optional<ClassEntity> classEntity = classRepository.findByGradeAndClassroom(grade, classroom);
        if (classEntity.isEmpty()) {
            throw new ClassNotExistsException("The class does not exist.");
        }
        return classEntity.get().getId();
    }

    public void checkIfGradeIsValid(Integer grade) {
        if (grade < 1 || grade > 9) {
            throw new GradeInvalidException("Grade is invalid.");
        }
    }

    public void checkIfClassroomIsValid(Integer classroom) {
        if (classroom < 1 || classroom > 20) {
            throw new ClassroomInvalidException("Classroom is invalid.");
        }
    }
}
