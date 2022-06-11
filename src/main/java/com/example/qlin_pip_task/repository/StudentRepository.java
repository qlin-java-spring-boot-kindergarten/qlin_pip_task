package com.example.qlin_pip_task.repository;

import com.example.qlin_pip_task.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {

    List<StudentEntity> findAllByName(String name);

    @Query(value = "SELECT array_agg(student_id ORDER BY student_id asc) FROM homework GROUP BY  homework_type ORDER BY homework_type asc;", nativeQuery = true)
    List<Integer> getHomework(Integer studentId);

}
