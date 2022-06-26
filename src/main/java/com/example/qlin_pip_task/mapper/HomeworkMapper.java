package com.example.qlin_pip_task.mapper;

import com.example.qlin_pip_task.dto.request.HomeworkAnswerSubmitRequest;
import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.StudentHomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HomeworkMapper {

    @Mapping(target = "id", ignore = true)
    StudentHomeworkEntity studentHomeworkRequestToEntity(StudentHomeworkSubmitRequest studentHomeworkSubmitRequest);

    HomeworkEntity homeworkRequestToEntity(HomeworkSubmitRequest homeworkSubmitRequest);

    HomeworkIdResponse homeworkEntityToHomeworkIdResponse(StudentHomeworkEntity studentHomeworkEntity);

    StudentHomeworkEntity homeworkAnswerRequestToEntity(HomeworkAnswerSubmitRequest homeworkAnswerSubmitRequest);


}
