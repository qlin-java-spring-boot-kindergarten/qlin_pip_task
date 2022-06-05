package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.response.StudentResponse;
import com.example.qlin_pip_task.service.StudentsService;
import com.example.qlin_pip_task.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class GetStudentsController {

    private final StudentsService studentsService;

    private final ValidationService validationService;

    @GetMapping
    public ResponseEntity<StudentResponse> getStudentsData() {
        return ResponseEntity.ok(studentsService.getAllStudentsResponse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<StudentResponse.Student>> getTheStudentData(@PathVariable Integer id) {
        Optional<StudentResponse.Student> theStudent = validationService.getTheExistedStudentData(id);
        return ResponseEntity.ok(theStudent);
    }
}