package com.example.qlin_pip_task.mapper;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HomeworkMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    HomeworkEntity homeworkRequestToEntity(HomeworkSubmitRequest homeworkSubmitRequest);

}
