package com.example.qlin_pip_task.repository;

import com.example.qlin_pip_task.dto.StudentsData;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Data
@RequiredArgsConstructor
public class StudentsRepository {

    public List<StudentsData> getAllData() {

        List<StudentsData> studentsDataList = new ArrayList<>();

        StudentsData studentsDataOne = new StudentsData();
        studentsDataOne.setId(1);
        studentsDataOne.setName("aaaaa");
        studentsDataOne.setGrade("3");
        studentsDataOne.setClassroom("1");

        StudentsData studentsDataTwo = new StudentsData();
        studentsDataTwo.setId(2);
        studentsDataTwo.setName("bbbbb");
        studentsDataTwo.setGrade("1");
        studentsDataTwo.setClassroom("1");

        studentsDataList.add(studentsDataOne);
        studentsDataList.add(studentsDataTwo);
        return studentsDataList;
    }
}
