package com.example.qlin_pip_task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class StudentHomeworkResponses {

    private List<StudentHomeworkResponse> homeworkList;

    @Data
    @Builder
    @AllArgsConstructor
    public static class StudentHomeworkResponse {
        private Integer id;
        private Integer homeworkId;
        private String content;
    }
}
