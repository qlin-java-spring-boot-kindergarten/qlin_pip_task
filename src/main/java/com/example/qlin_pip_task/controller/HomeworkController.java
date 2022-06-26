package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.request.HomeworkAnswerSubmitRequest;
import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentHomeworkGroupByIdAndDateAndClassResponses;
import com.example.qlin_pip_task.dto.response.StudentHomeworkIdResponse;
import com.example.qlin_pip_task.service.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/homework")
public class HomeworkController {

    private final HomeworkService homeworkService;

    @PostMapping
    public ResponseEntity<HomeworkIdResponse> createHomework(@RequestBody HomeworkSubmitRequest homeworkSubmitRequest) {
        return ResponseEntity.ok(homeworkService.createHomework(homeworkSubmitRequest));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<StudentHomeworkIdResponse>
    createStudentHomework(@PathVariable Integer id, @RequestBody HomeworkAnswerSubmitRequest homeworkAnswerSubmitRequest) {
        return ResponseEntity.ok(homeworkService.createStudentHomework(id, homeworkAnswerSubmitRequest));
    }

    @GetMapping
    public ResponseEntity<StudentHomeworkGroupByIdAndDateAndClassResponses>
    getStudentHomeworkByHomeworkIdAndClassIdAndDate(@RequestParam Map<String, String> queryMap) {
        return ResponseEntity.ok(homeworkService.getStudentHomeworkByHomeworkIdAndClassIdAndDate(queryMap));
    }

}

