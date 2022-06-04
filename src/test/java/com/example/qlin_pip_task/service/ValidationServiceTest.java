package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.StudentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

    @Mock
    private StudentResponse.Student student;

    @InjectMocks
    private ValidationService validationService;

    @Test
    void should_return_false_when_name_is_null(){
        boolean b = validationService.checkIfStudentDataIsValid(
                StudentResponse.Student.builder()
                        .name(null)
                        .id(1)
                        .classroom(1)
                        .grade(1)
                        .build());
        assertThat(b, is(false));
    }

}