package com.example.qlin_pip_task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroupsByHomeworkTypeResponses {
//    private List<List<StudentResponses.StudentResponse>> homework;
    Map<String, List<String>> homework;

}


// {
//
//     homework_group: {
//              {homework_type 1: [student1,xxx]}
//              {homework_type 2: [student1,xxx]}
//      }
//
// }