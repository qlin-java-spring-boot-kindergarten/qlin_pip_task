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
public class HomeworkResponses {

    private List<HomeworkResponse> homeworkList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Component
    public static class HomeworkResponse {
        private Integer id;
        private Integer homeworkId;
        private String content;
    }
}
