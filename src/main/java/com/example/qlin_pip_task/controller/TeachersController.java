package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.service.TeachersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeachersController {

    private final TeachersService teachersService;


    @PostMapping("/homework")
    public ResponseEntity<HomeworkIdResponse> createNewHomework(@RequestBody HomeworkSubmitRequest homeworkSubmitRequest) {
        return ResponseEntity.ok(teachersService.save(homeworkSubmitRequest));
    }


}

