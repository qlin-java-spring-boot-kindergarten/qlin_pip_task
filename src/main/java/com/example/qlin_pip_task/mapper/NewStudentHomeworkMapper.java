package com.example.qlin_pip_task.mapper;

import com.example.qlin_pip_task.dto.request.NewStudentHomeworkSubmitRequest;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewStudentHomeworkMapper {

    @Mapping(target = "id", ignore = true)
    StudentHomeworkEntity homeworkRequestToEntity(NewStudentHomeworkSubmitRequest newStudentHomeworkSubmitRequest);


}
