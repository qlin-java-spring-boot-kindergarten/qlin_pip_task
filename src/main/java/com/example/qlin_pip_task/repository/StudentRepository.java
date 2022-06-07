package com.example.qlin_pip_task.repository;

import com.example.qlin_pip_task.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {

    StudentEntity findByName(String name);

}
