package com.example.qlin_pip_task.mapper;

import com.example.qlin_pip_task.dto.request.StudentHomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentHomeworkMapper {

    @Mapping(target = "id", ignore = true)
    StudentHomeworkEntity homeworkRequestToEntity(StudentHomeworkSubmitRequest studentHomeworkSubmitRequest);

    HomeworkIdResponse homeworkEntityToHomeworkIdResponse(StudentHomeworkEntity studentHomeworkEntity);


}
