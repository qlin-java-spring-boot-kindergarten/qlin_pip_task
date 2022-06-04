package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.StudentResponse;
import com.example.qlin_pip_task.service.StudentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudentsController {

    private final StudentsService studentsService;

    @GetMapping("/students")
    public ResponseEntity<StudentResponse> getStudentsData() {
        return ResponseEntity.ok(studentsService.getAllStudentsResponse());
    }

    @PostMapping("/students")
    public ResponseEntity<String> receiveStudentData(@RequestBody StudentResponse.Student student){
        String saveData = studentsService.save(student);
        return ResponseEntity.ok(saveData);
    }

}
