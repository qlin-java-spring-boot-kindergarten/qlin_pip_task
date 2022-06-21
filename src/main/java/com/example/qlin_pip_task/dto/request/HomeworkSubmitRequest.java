package com.example.qlin_pip_task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class HomeworkSubmitRequest {
    private Integer teacherId;
    private Integer classId;
    private String description;
}
