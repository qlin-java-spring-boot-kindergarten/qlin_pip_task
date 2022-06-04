package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.StudentResponse;
import com.example.qlin_pip_task.service.StudentsService;
import com.example.qlin_pip_task.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StudentsController {

    private final StudentsService studentsService;

    private final ValidationService validationService;

    @GetMapping("/students")
    public ResponseEntity<StudentResponse> getStudentsData() {
        return ResponseEntity.ok(studentsService.getAllStudentsResponse());
    }

    @PostMapping("/students")
    public ResponseEntity<Map<String, Object>> receiveStudentData(@RequestBody @Validated StudentResponse.Student student) {
        validationService.checkIfStudentDataIsValid(student);
        String saveData = studentsService.save(student);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", saveData);
        return ResponseEntity.ok(map);
    }

}
