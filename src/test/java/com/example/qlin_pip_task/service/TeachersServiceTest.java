package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
import com.example.qlin_pip_task.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


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

}