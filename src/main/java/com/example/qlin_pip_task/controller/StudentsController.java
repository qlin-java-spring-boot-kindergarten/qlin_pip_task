package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.service.StudentsService;
import com.example.qlin_pip_task.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentsController {

    private final StudentsService studentsService;

    private final ValidationService validationService;

    @GetMapping
    public ResponseEntity<StudentResponses> getStudentsData() {
        return ResponseEntity.ok(studentsService.getAllStudentsResponse());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> receiveStudentRequest(@RequestBody @Validated StudentSubmitRequest studentSubmitRequest) {
        validationService.validateStudentData(studentSubmitRequest);
        String savedStudentData = studentsService.save(studentSubmitRequest);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", savedStudentData);
        return ResponseEntity.ok(map);
    }


    @GetMapping("/{id}")
    public ResponseEntity<StudentResponses> getTheStudentData(@PathVariable Integer id) {
        theStudentResponse = validationService.getTheExistedStudentData(id);
        return ResponseEntity.ok(theStudentRepsons);
    }

}

