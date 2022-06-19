package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeworkService {
    public HomeworkIdResponse save(HomeworkSubmitRequest homeworkSubmitRequest) {
        return HomeworkIdResponse.builder().build();
    }
}
