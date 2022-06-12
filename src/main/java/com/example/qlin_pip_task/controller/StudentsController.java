package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.StudentGroupsByHomeworkTypeResponses;
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
    public ResponseEntity<Map<String, Integer>> receiveStudentRequest(@RequestBody @Validated StudentSubmitRequest studentSubmitRequest) {
        validationService.validateStudentData(studentSubmitRequest);
        Integer savedStudentData = studentsService.save(studentSubmitRequest);
        HashMap<String, Integer> map = new HashMap<>();
        map.put("id", savedStudentData);
        return ResponseEntity.ok(map);
    }


    @GetMapping("/{id}")
    public ResponseEntity<StudentResponses.StudentResponse> getTheStudentData(@PathVariable Integer id) {
        return ResponseEntity.ok(studentsService.getTheStudentResponse(id));
    }
//
//    @GetMapping(params = "name")
//    public StudentResponses getTheStudentByName(@RequestParam Map<String, String> queryMap){
//        return studentsService.getTheStudentResponseByName(queryMap);
//    }


    @PostMapping("/{id}/homework")
    public ResponseEntity<Integer> receiveHomeRequest(@PathVariable Integer id, @RequestBody HomeworkSubmitRequest homeworkSubmitRequest) {
        return ResponseEntity.ok(studentsService.submitStudentHomework(id, homeworkSubmitRequest));
    }

    @GetMapping("/group-by-homework")
    public ResponseEntity<StudentGroupsByHomeworkTypeResponses> getHomeworkGroup(){
        return ResponseEntity.ok(studentsService.getStudentGroupsByHomewrokTypes());
    }
}

