package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.entity.ClassEntity;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
import com.example.qlin_pip_task.exception.ClassIdInvalidException;
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

    public ClassEntity getClassEntityById(Integer classId) {
        Optional<ClassEntity> optionalClassEntity = classRepository.findById(classId);
        if (optionalClassEntity.isEmpty()) {
            throw new ClassNotExistsException("The class does not exist.");
        }
        return optionalClassEntity.get();
    }

    public void checkIfClassIdIsValid(Integer classId) {
        if (classId == null) {
            throw new ClassIdInvalidException("Class ID cannot be null.");
        }
        Optional<ClassEntity> optionalClassEntity = classRepository.findById(classId);
        if (optionalClassEntity.isEmpty()) {
            throw new ClassNotExistsException("The class does not exist.");
        }
    }

    public void checkIfClassIsTheSame(StudentHomeworkEntity studentHomeworkEntity, Integer classId) {
        if (!studentHomeworkEntity.getStudentEntity().getClassId().equals(classId)) {
            throw new ClassIdInvalidException("The class id does not match the student id provided.");
        }
    }
}
