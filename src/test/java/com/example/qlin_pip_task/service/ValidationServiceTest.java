package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

    @InjectMocks
    private ValidationService validationService;

    @Test
    void should_throw_name_invalid_exception_when_name_is_null() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> validationService.validateStudentData(
                StudentSubmitRequest.builder()
                        .name(null)
                        .classroom(1)
                        .grade(1)
                        .build()));
        assertTrue(exception.getMessage().contains("Name is invalid."));
    }

    @Test
    void should_throw_name_invalid_exception_when_name_is_empty() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> validationService.validateStudentData(
                StudentSubmitRequest.builder()
                        .name("")
                        .classroom(1)
                        .grade(1)
                        .build()));
        assertTrue(exception.getMessage().contains("Name is invalid."));
    }

    @Test
    void should_throw_grade_invalid_exception_when_grade_is_smaller_than_one() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> validationService.validateStudentData(
                StudentSubmitRequest.builder()
                        .name("name")
                        .classroom(1)
                        .grade(0)
                        .build()));
        assertTrue(exception.getMessage().contains("Grade is invalid."));
    }

    @Test
    void should_throw_grade_invalid_exception_when_grade_is_larger_than_nine() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> validationService.validateStudentData(
                StudentSubmitRequest.builder()
                        .name("name")
                        .classroom(1)
                        .grade(10)
                        .build()));
        assertTrue(exception.getMessage().contains("Grade is invalid."));
    }

    @Test
    void should_throw_classroom_invalid_exception_when_classroom_is_smaller_than_one() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> validationService.validateStudentData(
                StudentSubmitRequest.builder()
                        .name("name")
                        .classroom(0)
                        .grade(1)
                        .build()));
        assertTrue(exception.getMessage().contains("Classroom is invalid."));
    }

    @Test
    void should_throw_classroom_invalid_exception_when_classroom_is_larger_than_twenty() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> validationService.validateStudentData(
                StudentSubmitRequest.builder()
                        .name("name")
                        .classroom(21)
                        .grade(1)
                        .build()));
        assertTrue(exception.getMessage().contains("Classroom is invalid."));
    }

}