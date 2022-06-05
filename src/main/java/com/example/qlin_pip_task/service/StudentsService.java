package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.StudentResponse;
import com.example.qlin_pip_task.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<StudentResponse.Student> getTheStudentResponse(Integer id) {
        return studentRepository.findById(id);
    }

    public void handle(StudentResponse.Student student, StudentSubmitRequest studentSubmitRequest) {
        student.setName(studentSubmitRequest.getName());
        student.setGrade(studentSubmitRequest.getGrade());
        student.setClassroom(studentSubmitRequest.getClassroom());
    }
}
