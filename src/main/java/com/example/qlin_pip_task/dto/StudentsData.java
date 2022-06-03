package com.example.qlin_pip_task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentsData {
    private Integer id;
    private String name;
    private String grade;
    private String classroom;
}
