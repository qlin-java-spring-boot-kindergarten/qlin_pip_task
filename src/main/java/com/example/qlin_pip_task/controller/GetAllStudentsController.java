package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.response.StudentResponse;
import com.example.qlin_pip_task.service.StudentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class GetAllStudentsController {

    private final StudentsService studentsService;

    @GetMapping
    public ResponseEntity<StudentResponse> getStudentsData() {
        return ResponseEntity.ok(studentsService.getAllStudentsResponse());
    }
}