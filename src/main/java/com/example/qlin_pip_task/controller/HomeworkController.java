package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.service.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<HomeworkIdResponse> createHomework(@RequestBody HomeworkSubmitRequest homeworkSubmitRequest) {
        return ResponseEntity.ok(homeworkService.createHomework(homeworkSubmitRequest));
    }

}

