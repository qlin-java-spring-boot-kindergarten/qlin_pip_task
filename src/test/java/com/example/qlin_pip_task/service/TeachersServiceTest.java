package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.entity.ClassEntity;
import com.example.qlin_pip_task.exception.ClassIdInvalidException;
import com.example.qlin_pip_task.exception.ClassNotExistsException;
import com.example.qlin_pip_task.exception.DescriptionInvalidException;
import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
import com.example.qlin_pip_task.repository.ClassRepository;
import com.example.qlin_pip_task.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TeachersServiceTest {

    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private ClassRepository classRepository;

    @InjectMocks
    private TeachersService teachersService;

    @Test
    void should_throw_teacher_id_invalid_when_id_is_null() {
        when(classRepository.findById(1)).thenReturn(Optional.of(ClassEntity.builder().id(1).build()));
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().classId(1).teacherId(null).description("this is a homework").build();
        Exception exception = assertThrows(TeacherIdInvalidException.class, () -> teachersService.save(request));

        assertTrue(exception.getMessage().contains("Teacher id is invalid."));
    }

    @Test
    void should_throw_teacher_id_invalid_when_teacher_entity_is_empty() {
        when(classRepository.findById(1)).thenReturn(Optional.of(ClassEntity.builder().id(1).build()));
        when(teacherRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TeacherIdInvalidException.class, () ->
                teachersService.save(HomeworkSubmitRequest.builder().classId(1).teacherId(99).description("this is a homework").build()));

        assertTrue(exception.getMessage().contains("Teacher id is not found."));
    }

    @Test
    void should_throw_description_invalid_exception_when_description_is_null() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(1).description(null).build();
        Exception exception = assertThrows(DescriptionInvalidException.class, () -> teachersService.save(request));

        assertTrue(exception.getMessage().contains("Description cannot be null."));
    }

    @Test
    void should_throw_description_invalid_exception_when_description_is_empty() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(1).description("").build();
        Exception exception = assertThrows(DescriptionInvalidException.class, () -> teachersService.save(request));

        assertTrue(exception.getMessage().contains("Description cannot be empty."));
    }

    @Test
    void should_throw_description_invalid_exception_when_description_has_only_whitespace() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(1).description("           ").build();
        Exception exception = assertThrows(DescriptionInvalidException.class, () -> teachersService.save(request));

        assertTrue(exception.getMessage().contains("Description cannot be empty."));
    }

    @Test
    void should_throw_class_id_invalid_exception_when_class_id_is_null() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(1).classId(null).description("test").build();
        Exception exception = assertThrows(ClassIdInvalidException.class, () -> teachersService.save(request));

        assertTrue(exception.getMessage().contains("Class ID cannot be null."));
    }

    @Test
    void should_throw_class_not_exists_exception_when_it_does_not_exist_in_the_table() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(1).classId(1).description("test").build();
        when(classRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ClassNotExistsException.class, () -> teachersService.save(request));
        assertTrue(exception.getMessage().contains("The class does not exist."));
    }

}