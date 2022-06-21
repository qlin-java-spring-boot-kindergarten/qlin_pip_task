package com.example.qlin_pip_task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NewStudentHomeworkSubmitRequest {
    private String content;
    private Integer classId;
    private Integer studentId;
}
