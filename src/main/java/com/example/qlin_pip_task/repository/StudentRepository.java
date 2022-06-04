package com.example.qlin_pip_task.repository;

import com.example.qlin_pip_task.dto.StudentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentResponse.Student, Integer> {

}
