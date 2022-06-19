package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.exception.DescriptionInvalidException;
import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
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

    @InjectMocks
    private TeachersService teachersService;

    @Test
    void should_throw_teacher_id_invalid_when_id_is_null() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(null).description("this is a homework").build();
        Exception exception = assertThrows(TeacherIdInvalidException.class, () -> teachersService.save(request));

        assertTrue(exception.getMessage().contains("Teacher id is invalid."));
    }

    @Test
    void should_throw_teacher_id_invalid_when_teacher_entity_is_empty() {
        when(teacherRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TeacherIdInvalidException.class, () ->
                teachersService.save(HomeworkSubmitRequest.builder().teacherId(99).description("this is a homework").build()));

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

}