package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.request.StudentHomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentGroupsByHomeworkTypeResponses;
import com.example.qlin_pip_task.dto.response.StudentIdResponse;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.service.StudentService;
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
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<StudentResponses> getStudents() {
        return ResponseEntity.ok(studentService.getStudents());
    }

    @PostMapping
    public ResponseEntity<StudentIdResponse> createStudent(@RequestBody StudentSubmitRequest studentSubmitRequest) {
        studentService.validateStudentData(studentSubmitRequest);
        return ResponseEntity.ok(studentService.createStudent(studentSubmitRequest));
    }


    @GetMapping("/{id}")
    public ResponseEntity<StudentResponses.StudentResponse> getStudentById(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping(params = "name")
    public ResponseEntity<StudentResponses> getStudentByName(@RequestParam Map<String, String> queryMap) {
        return ResponseEntity.ok(studentService.getStudentByName(queryMap));
    }


    @PostMapping("/{id}/homework")
    public ResponseEntity<HomeworkIdResponse> createStudentHomework(@PathVariable Integer id, @RequestBody StudentHomeworkSubmitRequest studentHomeworkSubmitRequest) {
        return ResponseEntity.ok(studentService.createStudentHomework(id, studentHomeworkSubmitRequest));
    }

    @GetMapping("/group-by-homework")
    public ResponseEntity<StudentGroupsByHomeworkTypeResponses> getStudentGroupsByHomework() {
        return ResponseEntity.ok(studentService.getStudentGroupsByHomework());
    }


    @PutMapping("/{id}/homework")
    public ResponseEntity<Void> updateStudentHomework(@PathVariable Integer id, @RequestBody StudentHomeworkSubmitRequest studentHomeworkSubmitRequest) {
        studentService.updateStudentHomework(id, studentHomeworkSubmitRequest);
        return ResponseEntity.noContent().build();
    }
}

