package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.StudentResponse;
import com.example.qlin_pip_task.exception.NameInvalidException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Executable;

import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

    @InjectMocks
    private ValidationService validationService;

    @Test
    void should_throw_name_invalid_exception_when_name_is_null(){
        Exception exception = assertThrows(NameInvalidException.class, ()->{
                validationService.checkIfStudentDataIsValid(
                StudentResponse.Student.builder()
                        .name(null)
                        .id(1)
                        .classroom(1)
                        .grade(1)
                        .build());
        });
        assertTrue(exception.getMessage().contains("Name is invalid."));
    }

}