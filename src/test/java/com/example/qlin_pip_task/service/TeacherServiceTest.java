package com.example.qlin_pip_task.service;


import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
import com.example.qlin_pip_task.exception.TeacherNoFoundException;
import com.example.qlin_pip_task.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void should_throw_teacher_id_invalid_exception_given_null_teacher_id() {
        Exception exception = assertThrows(TeacherIdInvalidException.class, () -> teacherService.checkIfTeacherIdIsValid(null));
        assertThat(exception.getMessage(), is("Teacher id is invalid."));
    }

    @Test
    void should_throw_teacher_not_found_exception_given_teacher_id() {
        when(teacherRepository.existsById(1)).thenReturn(false);
        Exception exception = assertThrows(TeacherNoFoundException.class, () -> teacherService.checkIfTeacherIdIsValid(1));
        assertThat(exception.getMessage(), is("Teacher does not exist."));
    }

}