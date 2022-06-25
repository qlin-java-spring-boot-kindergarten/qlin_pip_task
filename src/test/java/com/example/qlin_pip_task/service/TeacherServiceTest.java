package com.example.qlin_pip_task.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidParameterException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void should_throw_teacher_id_invalid_exception_given_null_teacher_id() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> teacherService.checkIfTeacherIdIsValid(null));
        assertThat(exception.getMessage(), is("Teacher id is invalid."));
    }

}