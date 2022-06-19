package com.example.qlin_pip_task.mapper;

import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.ClassEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "studentEntity.id", target = "id")
    StudentResponses.StudentResponse entityToStudentResponse(StudentEntity studentEntity, ClassEntity classEntity);

}
