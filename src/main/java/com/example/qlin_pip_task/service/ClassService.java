package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.entity.ClassEntity;
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

}
