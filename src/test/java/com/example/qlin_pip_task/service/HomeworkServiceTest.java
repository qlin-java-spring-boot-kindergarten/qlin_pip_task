package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.NewStudentHomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.entity.ClassEntity;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.TeacherEntity;
import com.example.qlin_pip_task.exception.ClassIdInvalidException;
import com.example.qlin_pip_task.exception.ClassNotExistsException;
import com.example.qlin_pip_task.exception.DescriptionInvalidException;
import com.example.qlin_pip_task.exception.HomeworkIdInvalidException;
import com.example.qlin_pip_task.exception.StudentIdInvalidException;
import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.repository.ClassRepository;
import com.example.qlin_pip_task.repository.HomeworkRepository;
import com.example.qlin_pip_task.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class HomeworkServiceTest {

    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private ClassRepository classRepository;
    @Mock
    private HomeworkMapper homeworkMapper;
    @Mock
    private HomeworkRepository homeworkRepository;

    @InjectMocks
    private HomeworkService homeworkService;

    @Test
    void should_throw_teacher_id_invalid_when_id_is_null() {
        when(classRepository.findById(1)).thenReturn(Optional.of(ClassEntity.builder().id(1).build()));
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().classId(1).teacherId(null).description("this is a homework").build();
        Exception exception = assertThrows(TeacherIdInvalidException.class, () -> homeworkService.save(request));

        assertTrue(exception.getMessage().contains("Teacher id is invalid."));
    }

    @Test
    void should_throw_teacher_id_invalid_when_teacher_entity_is_empty() {
        when(classRepository.findById(1)).thenReturn(Optional.of(ClassEntity.builder().id(1).build()));
        when(teacherRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TeacherIdInvalidException.class, () ->
                homeworkService.save(HomeworkSubmitRequest.builder().classId(1).teacherId(99).description("this is a homework").build()));

        assertTrue(exception.getMessage().contains("Teacher id is not found."));
    }

    @Test
    void should_throw_description_invalid_exception_when_description_is_null() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(1).description(null).build();
        Exception exception = assertThrows(DescriptionInvalidException.class, () -> homeworkService.save(request));

        assertTrue(exception.getMessage().contains("Description cannot be null."));
    }

    @Test
    void should_throw_description_invalid_exception_when_description_is_empty() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(1).description("").build();
        Exception exception = assertThrows(DescriptionInvalidException.class, () -> homeworkService.save(request));

        assertTrue(exception.getMessage().contains("Description cannot be empty."));
    }

    @Test
    void should_throw_description_invalid_exception_when_description_has_only_whitespace() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(1).description("           ").build();
        Exception exception = assertThrows(DescriptionInvalidException.class, () -> homeworkService.save(request));

        assertTrue(exception.getMessage().contains("Description cannot be empty."));
    }

    @Test
    void should_throw_class_id_invalid_exception_when_class_id_is_null() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(1).classId(null).description("test").build();
        Exception exception = assertThrows(ClassIdInvalidException.class, () -> homeworkService.save(request));

        assertTrue(exception.getMessage().contains("Class ID cannot be null."));
    }

    @Test
    void should_throw_class_not_exists_exception_when_it_does_not_exist_in_the_table() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(1).classId(1).description("test").build();
        when(classRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ClassNotExistsException.class, () -> homeworkService.save(request));
        assertTrue(exception.getMessage().contains("The class does not exist."));
    }

    @Test
    void should_save_homework_and_return_homework_id_given_valid_teacher_id_and_class_id_and_description() {
        HomeworkSubmitRequest homeworkSubmitRequest =
                HomeworkSubmitRequest.builder().classId(1).description("test").teacherId(11).build();
        ClassEntity classEntity = ClassEntity.builder().id(1).classroom(1).classroom(1).build();
        when(classRepository.findById(1)).thenReturn(Optional.of(classEntity));
        TeacherEntity teacherEntity = TeacherEntity.builder().id(11).classId(1).name("teacher_one").build();
        when(teacherRepository.findById(11)).thenReturn(Optional.of(teacherEntity));
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherId(11).description("test").classId(1).build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest))
                .thenReturn(homeworkEntity);
        HomeworkEntity savedHomeworkEntity = HomeworkEntity.builder().id(99).teacherId(11).description("test").classId(1).build();
        when(homeworkRepository.save(homeworkEntity)).thenReturn(savedHomeworkEntity);

        HomeworkIdResponse result = homeworkService.save(homeworkSubmitRequest);

        assertThat(result.getId(), is(99));
    }

    @Test
    void should_throw_homework_id_invalid_exception_when_homework_id_is_not_found() {
        NewStudentHomeworkSubmitRequest request =
                NewStudentHomeworkSubmitRequest.builder().studentId(1).content("test").build();
        when(homeworkRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(HomeworkIdInvalidException.class,
                () -> homeworkService.getStudentHomeworkIdResponse(1, request));
        assertTrue(exception.getMessage().contains("The homework id is not found."));
    }

    @Test
    void should_throw_student_id_invalid_exception_when_student_id_is_null() {
        NewStudentHomeworkSubmitRequest request =
                NewStudentHomeworkSubmitRequest.builder().studentId(null).content("test").build();
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().description("this is a homework").build();
        when(homeworkRepository.findById(1)).thenReturn(Optional.of(homeworkEntity));
        Exception exception = assertThrows(StudentIdInvalidException.class,
                () -> homeworkService.getStudentHomeworkIdResponse(1, request));
        assertTrue(exception.getMessage().contains("The student id is null."));
    }

}