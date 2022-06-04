package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.Student;
import com.example.qlin_pip_task.dto.StudentsResponse;
import com.example.qlin_pip_task.repository.StudentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentsService {

    private final StudentsRepository studentsRepository;
    private final StudentsResponse studentsResponse;

    public StudentsResponse getStudentsResponse() {
        List<Student> studentData = studentsRepository.getAllData();
        studentsResponse.setData(studentData);
        return studentsResponse;
    }
}
