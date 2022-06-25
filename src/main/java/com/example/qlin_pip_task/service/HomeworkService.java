package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.TeacherEntity;
import com.example.qlin_pip_task.exception.DescriptionInvalidException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.repository.HomeworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkMapper homeworkMapper;

    private final HomeworkRepository homeworkRepository;

    private final TeacherService teacherService;

    public HomeworkIdResponse createHomework(HomeworkSubmitRequest homeworkSubmitRequest) {
        Integer teacherId = homeworkSubmitRequest.getTeacherId();
        TeacherEntity teacherEntity = teacherService.getNonNullTeacherEntity(teacherId);
        HomeworkEntity homeworkEntity = homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest);
        homeworkEntity.setTeacherEntity(teacherEntity);
        String description = homeworkSubmitRequest.getDescription();
        checkIfDescriptionIsValid(description);
        HomeworkEntity savedHomeworkEntity = homeworkRepository.save(homeworkEntity);
        return HomeworkIdResponse.builder().id(savedHomeworkEntity.getId()).build();
    }

    private void checkIfDescriptionIsValid(String description) {
        if (Objects.isNull(description)) {
            throw new DescriptionInvalidException("Description is null");
        }
        if (description.isBlank()) {
            throw new DescriptionInvalidException("Description is empty.");
        }
        if (homeworkRepository.existsByDescription(description)) {
            throw new DescriptionInvalidException("Description is duplicated.");
        }
    }
}
