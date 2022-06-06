package com.example.qlin_pip_task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class StudentResponses {

    private List<StudentResponse> data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Component
    public static class StudentResponse {
        private Integer id;
        private String name;
        private Integer grade;
        private Integer classroom;

    }
}
