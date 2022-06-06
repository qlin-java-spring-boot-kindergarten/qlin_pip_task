package com.example.qlin_pip_task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSubmitRequest {
    private String name;
    private Integer grade;
    private Integer classroom;
}
