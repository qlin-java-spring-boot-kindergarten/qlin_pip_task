package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.Student;
import com.example.qlin_pip_task.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentsService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudentsResponse() {
        List<Student> studentList = studentRepository.findAll();
        return studentList;
    }
}
