package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.TeacherEntity;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.repository.HomeworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkMapper homeworkMapper;

    private final HomeworkRepository homeworkRepository;

    public HomeworkIdResponse createHomework(HomeworkSubmitRequest homeworkSubmitRequest) {
        Integer teacherId = homeworkSubmitRequest.getTeacherId();
        HomeworkEntity homeworkEntity = homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest);
        homeworkEntity.setTeacherEntity(TeacherEntity.builder().id(teacherId).build());
        HomeworkEntity savedHomeworkEntity = homeworkRepository.save(homeworkEntity);
        return HomeworkIdResponse.builder().id(savedHomeworkEntity.getId()).build();
    }
}
