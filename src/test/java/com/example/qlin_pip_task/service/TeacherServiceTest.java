package com.example.qlin_pip_task.service;


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
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void should_throw_teacher_id_invalid_when_teacher_entity_is_empty() {
        when(teacherRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TeacherIdInvalidException.class,
                () -> teacherService.checkIfTeacherEntityExists(99));

        assertTrue(exception.getMessage().contains("Teacher is not found."));
    }
}