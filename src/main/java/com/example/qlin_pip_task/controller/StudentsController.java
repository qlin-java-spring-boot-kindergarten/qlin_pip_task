package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.Student;
import com.example.qlin_pip_task.service.StudentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudentsController {

    private final StudentsService studentsService;

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudentsData() {
        List<Student> studentsResponse = studentsService.getAllStudentsResponse();
        return ResponseEntity.ok(studentsResponse);
    }

}
