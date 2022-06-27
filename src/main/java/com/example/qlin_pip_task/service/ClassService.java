package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.entity.ClassEntity;
import com.example.qlin_pip_task.exception.ClassNotExistsException;
import com.example.qlin_pip_task.exception.ClassroomInvalidException;
import com.example.qlin_pip_task.exception.GradeInvalidException;
import com.example.qlin_pip_task.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;

    public void checkIfClassroomStrIsValid(String classroomStr) {
        if (Objects.isNull(classroomStr)) {
            throw new ClassroomInvalidException("Classroom is null.");
        }
        try {
            Integer.parseInt(classroomStr);
        } catch (NumberFormatException e) {
            throw new ClassroomInvalidException("Classroom is invalid.");
        }
    }

    public void checkIfGradeStrIsValid(String gradeStr) {
        if (Objects.isNull(gradeStr)) {
            throw new GradeInvalidException("Grade is null.");
        }
        try {
            Integer.parseInt(gradeStr);
        } catch (NumberFormatException e) {
            throw new GradeInvalidException("Grade is invalid.");
        }
    }


    public Integer getClassId(Integer grade, Integer classroom) {
        checkIfGradeIsValid(grade);
        checkIfClassroomIsValid(classroom);
        Optional<ClassEntity> optionalClassEntity = classRepository.findByGradeAndClassroom(grade, classroom);
        if (optionalClassEntity.isEmpty()) {
            throw new ClassNotExistsException("The class does not exist.");
        }
        ClassEntity classEntity = optionalClassEntity.get();
        return classEntity.getId();
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
    // Assume that all the class data in the class table has been validated when the data is recorded
    // Therefore, no need to handle the empty Optional of ClassEntity

    public ClassEntity getClassEntityById(Integer classId) {
        Optional<ClassEntity> optionalClassEntity = classRepository.findById(classId);
        return optionalClassEntity.get();
    }
}
