package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.StudentResponse;
import com.example.qlin_pip_task.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentsService {

    private final StudentRepository studentRepository;

    private final StudentResponse studentResponse;

    public StudentResponse getAllStudentsResponse() {
        List<StudentResponse.Student> allStudentsData = studentRepository.findAll();
        studentResponse.setData(allStudentsData);
        return studentResponse;
    }

    public String save(StudentResponse.Student student){
        StudentResponse.Student studentData = studentRepository.save(student);
        return studentData.getId().toString();
    }
}
