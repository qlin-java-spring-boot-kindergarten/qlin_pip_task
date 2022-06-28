package com.example.qlin_pip_task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class StudentHomeworkGroupByIdAndDateAndClassResponses {
    private String homeworkId;
    private String grade;
    private String classroom;
    private String createdAt;
    private List<StudentHomeworkResponse> studentHomeworkList;

    @Data
    @Builder
    @AllArgsConstructor
    public static class StudentHomeworkResponse {
        private Integer studentId;
        private String content;
    }
}
