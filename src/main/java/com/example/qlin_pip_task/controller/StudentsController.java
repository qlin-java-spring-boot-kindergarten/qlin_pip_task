package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.request.StudentHomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentGroupsByHomeworkTypeResponses;
import com.example.qlin_pip_task.dto.response.StudentIdResponse;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.service.StudentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentsController {

    private final StudentsService studentsService;

    @GetMapping
    public ResponseEntity<StudentResponses> getStudents() {
        return ResponseEntity.ok(studentsService.getStudents());
    }

    @PostMapping
    public ResponseEntity<StudentIdResponse> createStudent(@RequestBody StudentSubmitRequest studentSubmitRequest) {
        studentsService.validateStudentData(studentSubmitRequest);
        return ResponseEntity.ok(studentsService.createStudent(studentSubmitRequest));
    }


    @GetMapping("/{id}")
    public ResponseEntity<StudentResponses.StudentResponse> getStudentById(@PathVariable Integer id) {
        return ResponseEntity.ok(studentsService.getStudentById(id));
    }

    @GetMapping(params = "name")
    public ResponseEntity<StudentResponses> getStudentByName(@RequestParam Map<String, String> queryMap) {
        return ResponseEntity.ok(studentsService.getStudentByName(queryMap));
    }


    @PostMapping("/{id}/homework")
    public ResponseEntity<HomeworkIdResponse> createStudentHomework(@PathVariable Integer id, @RequestBody StudentHomeworkSubmitRequest studentHomeworkSubmitRequest) {
        return ResponseEntity.ok(studentsService.createStudentHomework(id, studentHomeworkSubmitRequest));
    }

    @GetMapping("/group-by-homework")
    public ResponseEntity<StudentGroupsByHomeworkTypeResponses> getStudentGroupsByHomework() {
        return ResponseEntity.ok(studentsService.getStudentGroupsByHomework());
    }


    @PutMapping("/{id}/homework")
    public ResponseEntity<Void> updateStudentHomework(@PathVariable Integer id, @RequestBody StudentHomeworkSubmitRequest studentHomeworkSubmitRequest) {
        studentsService.updateStudentHomework(id, studentHomeworkSubmitRequest);
        return ResponseEntity.noContent().build();
    }
}

