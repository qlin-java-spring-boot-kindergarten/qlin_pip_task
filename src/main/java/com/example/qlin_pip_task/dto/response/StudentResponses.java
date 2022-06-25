package com.example.qlin_pip_task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class StudentResponses {

    private List<StudentResponse> data;

    @Data
    @Builder
    @AllArgsConstructor
    public static class StudentResponse {
        private Integer id;
        private String name;
        private Integer grade;
        private Integer classroom;
        private List<StudentHomeworkResponses.StudentHomeworkResponse> studentHomework;
    }
}
