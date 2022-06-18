package com.example.qlin_pip_task.mapper;

import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.StudentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentResponses.StudentResponse entityToStudentResponse(StudentEntity studentEntity);

}
