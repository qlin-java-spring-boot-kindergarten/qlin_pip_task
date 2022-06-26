package com.example.qlin_pip_task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class HomeworkAnswerSubmitRequest {
    private Integer studentId;
    private String content;
}
