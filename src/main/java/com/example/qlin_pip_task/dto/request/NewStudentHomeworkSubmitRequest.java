package com.example.qlin_pip_task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewStudentHomeworkSubmitRequest {
    private String content;
    private Integer studentId;
}
