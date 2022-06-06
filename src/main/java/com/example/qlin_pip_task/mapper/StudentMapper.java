package com.example.qlin_pip_task.mapper;

import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    StudentResponses.StudentResponse entityToStudentResponse(StudentEntity studentEntity);

    @Mapping(target = "id", ignore = true)
    StudentEntity requestToStudentEntity(StudentSubmitRequest  studentSubmitRequest);

    StudentResponses entitiesToStudentResponses(List<StudentEntity> studentEntities);

}
