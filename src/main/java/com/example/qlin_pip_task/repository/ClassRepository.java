package com.example.qlin_pip_task.repository;

import com.example.qlin_pip_task.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Integer> {

    Optional<ClassEntity> findByGradeAndClassroom(Integer grade, Integer classroom);

}
