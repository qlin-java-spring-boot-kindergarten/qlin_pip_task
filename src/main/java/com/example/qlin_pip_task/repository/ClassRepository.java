package com.example.qlin_pip_task.repository;

import com.example.qlin_pip_task.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Integer> {

    ClassEntity findClassEntityByGradeAndClassroom(Integer grade, Integer classroom);

}
