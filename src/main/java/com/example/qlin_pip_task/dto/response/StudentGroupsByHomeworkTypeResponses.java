package com.example.qlin_pip_task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroupsByHomeworkTypeResponses {
    private List<List<StudentResponses.StudentResponse>> homework;
}
