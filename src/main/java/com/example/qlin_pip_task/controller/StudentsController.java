package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.StudentsData;
import com.example.qlin_pip_task.dto.StudentsResponse;
import com.example.qlin_pip_task.service.StudentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudentsController {

    private final StudentsService studentsService;

    @GetMapping("/students")
    public ResponseEntity<StudentsResponse> getStudentsData() {
        StudentsResponse studentsResponse = studentsService.getStudentsResponse();
        return ResponseEntity.ok(studentsResponse);

    }

}
