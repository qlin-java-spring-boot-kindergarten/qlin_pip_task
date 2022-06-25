package com.example.qlin_pip_task.repository;

import com.example.qlin_pip_task.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TeacherRepository extends JpaRepository<TeacherEntity, Integer> {

}
