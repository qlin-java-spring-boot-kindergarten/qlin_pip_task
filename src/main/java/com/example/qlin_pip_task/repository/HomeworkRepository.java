package com.example.qlin_pip_task.repository;

import com.example.qlin_pip_task.entity.HomeworkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HomeworkRepository extends JpaRepository<HomeworkEntity, Integer> {


}
