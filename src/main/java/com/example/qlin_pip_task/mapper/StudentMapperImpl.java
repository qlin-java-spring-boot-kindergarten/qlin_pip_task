//package com.example.qlin_pip_task.mapper;
//
//import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
//import com.example.qlin_pip_task.dto.response.StudentResponses;
//import com.example.qlin_pip_task.entity.StudentEntity;
//
//import java.util.Optional;
//
//public class StudentMapperImpl implements StudentMapper{
//
////    @Override
////    public StudentResponses.StudentResponse entityToStudentResponse(Optional<StudentEntity> studentEntity) {
////        StudentResponses.StudentResponse studentResponse = new StudentResponses.StudentResponse();
////        studentResponse.setClassroom(studentEntity.get().getClassroom());
////        studentResponse.setGrade(studentEntity.get().getGrade());
////        studentResponse.setName(studentEntity.get().getName());
////        studentResponse.setId(studentEntity.get().getId());
////        return studentResponse;
////
////    }
//
//    @Override
//    public StudentResponses.StudentResponse entityToStudentResponse(Optional<StudentEntity> studentEntity) {
//        return null;
//    }
//
//    @Override
//    public StudentEntity requestToStudentEntity(StudentSubmitRequest studentSubmitRequest) {
//        StudentEntity studentEntity = new StudentEntity();
//        studentEntity.setClassroom(studentSubmitRequest.getClassroom());
//        studentEntity.setName(studentSubmitRequest.getName());
//        studentEntity.setGrade(studentEntity.getGrade());
//        return studentEntity;
//    }
//}
