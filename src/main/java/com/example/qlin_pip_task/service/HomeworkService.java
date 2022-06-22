package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.NewStudentHomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentHomeworkIdResponse;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
import com.example.qlin_pip_task.exception.DescriptionInvalidException;
import com.example.qlin_pip_task.exception.HomeworkIdInvalidException;
import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.mapper.NewStudentHomeworkMapper;
import com.example.qlin_pip_task.repository.HomeworkRepository;
import com.example.qlin_pip_task.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final HomeworkMapper homeworkMapper;
    private final TeacherService teacherService;
    private final StudentsService studentsService;
    private final StudentRepository studentRepository;
    private final ClassService classService;
    private final NewStudentHomeworkMapper newStudentHomeworkMapper;

    public HomeworkIdResponse save(HomeworkSubmitRequest homeworkSubmitRequest) {
        String description = homeworkSubmitRequest.getDescription();
        checkIfHomeworkDescriptionIsValid(description);
        Integer classId = homeworkSubmitRequest.getClassId();
        classService.checkIfClassIdIsValid(classId);
        Integer teacherId = homeworkSubmitRequest.getTeacherId();
        checkIfTeacherIdIsNull(teacherId);
        teacherService.checkIfTeacherEntityExists(teacherId);
        HomeworkEntity homeworkEntity = homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest);
        HomeworkEntity savedNewHomework = homeworkRepository.save(homeworkEntity);
        return HomeworkIdResponse.builder().id(savedNewHomework.getId()).build();
    }

    public StudentHomeworkIdResponse getStudentHomeworkIdResponse(Integer homeworkId, NewStudentHomeworkSubmitRequest newStudentHomeworkSubmitRequest) {
        Optional<HomeworkEntity> optionalHomeworkEntity = homeworkRepository.findById(homeworkId);
        if (optionalHomeworkEntity.isEmpty()) {
            throw new HomeworkIdInvalidException("The homework id is not found.");
        }
        Integer studentId = newStudentHomeworkSubmitRequest.getStudentId();
        studentsService.checkIfStudentIdIsNull(studentId);
        StudentEntity studentEntity = studentsService.getNotNullableStudentEntity(studentId);
        StudentHomeworkEntity studentHomeworkEntity =
                newStudentHomeworkMapper.homeworkRequestToEntity(newStudentHomeworkSubmitRequest);
        studentHomeworkEntity.setHomeworkId(homeworkId);
        Integer classId = newStudentHomeworkSubmitRequest.getClassId();
        studentHomeworkEntity.setStudentEntity(studentEntity);
        classService.checkIfClassIdIsValid(classId);
        classService.checkIfClassIsTheSame(studentHomeworkEntity, classId);
        List<StudentHomeworkEntity> homeworkEntityList = studentEntity.getStudentHomework();
        homeworkEntityList.add(studentHomeworkEntity);
        StudentEntity savedStudentEntity = studentRepository.save(studentEntity);
        Integer id = savedStudentEntity.getStudentHomework().get(homeworkEntityList.size() - 1).getId();
        return StudentHomeworkIdResponse.builder().id(id).build();

    }

    private void checkIfTeacherIdIsNull(Integer teacherId) {
        if (teacherId == null) {
            throw new TeacherIdInvalidException("Teacher id is invalid.");
        }
    }

    private void checkIfHomeworkDescriptionIsValid(String description) {
        if (description == null) {
            throw new DescriptionInvalidException("Description cannot be null.");
        }
        if (description.isBlank()) {
            throw new DescriptionInvalidException("Description cannot be empty.");
        }
    }
}
