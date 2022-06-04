package com.example.qlin_pip_task.repository;

import com.example.qlin_pip_task.dto.Student;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Data
@RequiredArgsConstructor
public class StudentsRepository {

    public List<Student> getAllData() {

        List<Student> studentList = new ArrayList<>();

        Student studentOne = new Student();
        studentOne.setId(1);
        studentOne.setName("aaaaa");
        studentOne.setGrade(3);
        studentOne.setClassroom(1);

        Student studentTwo = new Student();
        studentTwo.setId(2);
        studentTwo.setName("bbbbb");
        studentTwo.setGrade(1);
        studentTwo.setClassroom(1);

        studentList.add(studentOne);
        studentList.add(studentTwo);
        return studentList;
    }
}
