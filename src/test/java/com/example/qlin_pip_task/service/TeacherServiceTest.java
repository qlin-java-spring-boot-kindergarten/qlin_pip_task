package com.example.qlin_pip_task.service;


import com.example.qlin_pip_task.entity.TeacherEntity;
import com.example.qlin_pip_task.exception.TeacherIdInvalidException;
import com.example.qlin_pip_task.exception.TeacherNoFoundException;
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
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void should_throw_teacher_id_invalid_exception_given_null_teacher_id() {
        Exception exception = assertThrows(TeacherIdInvalidException.class, () -> teacherService.getNonNullTeacherEntity(null));
        assertThat(exception.getMessage(), is("Teacher id is invalid."));
    }

    @Test
    void should_throw_teacher_not_found_exception_given_teacher_id() {
        when(teacherRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(TeacherNoFoundException.class, () -> teacherService.getNonNullTeacherEntity(1));
        assertThat(exception.getMessage(), is("Teacher does not exist."));
    }

    @Test
    void should_return_teacher_entity_given_valid_id() {
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).name("teacher").classId(1).build();
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacherEntity));
        TeacherEntity result = teacherService.getNonNullTeacherEntity(1);
        assertThat(result.getId(), is(1));
        assertThat(result.getName(), is("teacher"));
        assertThat(result.getClassId(), is(1));
    }

}