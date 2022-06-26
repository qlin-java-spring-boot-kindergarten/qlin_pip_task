package com.example.qlin_pip_task.service;


import com.example.qlin_pip_task.dto.request.HomeworkAnswerSubmitRequest;
import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentHomeworkIdResponse;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
import com.example.qlin_pip_task.entity.TeacherEntity;
import com.example.qlin_pip_task.exception.DescriptionInvalidException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.repository.HomeworkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HomeworkServiceTest {
    @Mock
    private HomeworkMapper homeworkMapper;
    @Mock
    private HomeworkRepository homeworkRepository;
    @Mock
    private TeacherService teacherService;
    @Mock
    private StudentService studentService;

    @InjectMocks
    private HomeworkService homeworkService;


    @Test
    void should_return_homeworkId_when_create_new_homework_successfully() {
        HomeworkSubmitRequest homeworkSubmitRequest = HomeworkSubmitRequest.builder().teacherId(1).description("a homework").build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).build();
        when(teacherService.getNotNullTeacherEntity(1)).thenReturn(teacherEntity);
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherEntity(teacherEntity).description("a homework").build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest)).thenReturn(homeworkEntity);
        HomeworkEntity savedHomeworkEntity = HomeworkEntity.builder().id(99).teacherEntity(teacherEntity).description("a homework").build();
        when(homeworkRepository.save(homeworkEntity)).thenReturn(savedHomeworkEntity);

        HomeworkIdResponse result = homeworkService.createHomework(homeworkSubmitRequest);
        assertThat(result.getId(), is(99));
    }

    @Test
    void should_throw_description_invalid_exception_given_null_description() {
        HomeworkSubmitRequest homeworkSubmitRequest = HomeworkSubmitRequest.builder().teacherId(1).description(null).build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).build();
        when(teacherService.getNotNullTeacherEntity(1)).thenReturn(teacherEntity);
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherEntity(teacherEntity).description(null).build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest)).thenReturn(homeworkEntity);
        Exception exception = assertThrows(DescriptionInvalidException.class, () -> homeworkService.createHomework(homeworkSubmitRequest));
        assertThat(exception.getMessage(), is("Description is null"));
    }

    @Test
    void should_throw_description_invalid_exception_given_empty_description() {
        HomeworkSubmitRequest homeworkSubmitRequest = HomeworkSubmitRequest.builder().teacherId(1).description("").build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).build();
        when(teacherService.getNotNullTeacherEntity(1)).thenReturn(teacherEntity);
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherEntity(teacherEntity).description("").build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest)).thenReturn(homeworkEntity);
        Exception exception = assertThrows(DescriptionInvalidException.class, () -> homeworkService.createHomework(homeworkSubmitRequest));
        assertThat(exception.getMessage(), is("Description is empty."));
    }
    @Test
    void should_throw_description_invalid_exception_given_whitespace_only_description() {
        HomeworkSubmitRequest homeworkSubmitRequest = HomeworkSubmitRequest.builder().teacherId(1).description("     ").build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).build();
        when(teacherService.getNotNullTeacherEntity(1)).thenReturn(teacherEntity);
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherEntity(teacherEntity).description("      ").build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest)).thenReturn(homeworkEntity);
        Exception exception = assertThrows(DescriptionInvalidException.class, () -> homeworkService.createHomework(homeworkSubmitRequest));
        assertThat(exception.getMessage(), is("Description is empty."));
    }

    @Test
    void should_throw_description_invalid_exception_given_duplicated_description() {
        HomeworkSubmitRequest homeworkSubmitRequest = HomeworkSubmitRequest.builder().teacherId(1).description("a homework").build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).build();
        when(teacherService.getNotNullTeacherEntity(1)).thenReturn(teacherEntity);
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherEntity(teacherEntity).description("a homework").build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest)).thenReturn(homeworkEntity);
        when(homeworkRepository.existsByDescription("a homework")).thenReturn(true);

        Exception exception = assertThrows(DescriptionInvalidException.class, () -> homeworkService.createHomework(homeworkSubmitRequest));
        assertThat(exception.getMessage(), is("Description is duplicated."));
    }

    @Test
    void should_save_student_homework_and_return_its_id_given_valid_student_id_and_homework_content() {
        HomeworkAnswerSubmitRequest homeworkAnswerSubmitRequest =
                HomeworkAnswerSubmitRequest.builder().studentId(1).content("this is an answer").build();
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().id(3).studentHomework(new ArrayList<>()).build();
        when(homeworkRepository.findById(9)).thenReturn(Optional.of(homeworkEntity));
        StudentEntity studentEntity = StudentEntity.builder().id(1).name("student").classId(2).build();
        when(studentService.getNotNullStudentEntity(1)).thenReturn(studentEntity);
        StudentHomeworkEntity studentHomeworkEntity =
                StudentHomeworkEntity.builder().content("this is an answer").studentEntity(studentEntity).build();
        when(homeworkMapper.homeworkAnswerRequestToEntity(homeworkAnswerSubmitRequest)).thenReturn(studentHomeworkEntity);
        StudentHomeworkEntity savedStudentHomeworkEntity = StudentHomeworkEntity.builder().id(100)
                .studentEntity(StudentEntity.builder().id(1).classId(2).studentHomework(List.of(studentHomeworkEntity)).build()).build();
        HomeworkEntity savedHomeworkEntity = HomeworkEntity.builder().id(3).studentHomework(List.of(savedStudentHomeworkEntity)).build();
        when(homeworkRepository.save(homeworkEntity)).thenReturn(savedHomeworkEntity);

        StudentHomeworkIdResponse result = homeworkService.createStudentHomework(9, homeworkAnswerSubmitRequest);

        assertThat(result.getId(), is(100));

    }

}