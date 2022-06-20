package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.StudentHomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentHomeworkIdResponse;
import com.example.qlin_pip_task.service.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/homework")
public class HomeworkController {

    private final HomeworkService homeworkService;

    @PostMapping
    public ResponseEntity<HomeworkIdResponse> createNewHomework(@RequestBody HomeworkSubmitRequest homeworkSubmitRequest) {
        return ResponseEntity.ok(homeworkService.save(homeworkSubmitRequest));
    }


    @PostMapping("/{id}/submit")
    public ResponseEntity<StudentHomeworkIdResponse> getStudentData(@PathVariable Integer id,
                                                                    @RequestBody StudentHomeworkSubmitRequest studentHomeworkSubmitRequest) {
        return ResponseEntity.ok(homeworkService.getStudentHomeworkIdResponse(id, studentHomeworkSubmitRequest));
    }

}

