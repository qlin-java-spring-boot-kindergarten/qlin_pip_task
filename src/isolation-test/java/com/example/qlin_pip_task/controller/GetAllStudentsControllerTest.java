package com.example.qlin_pip_task.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class GetAllStudentsControllerTest extends IsolationTest{
    @Test
    void should_return_student_data_list_and_200_OK_status_code_when_call_get_students_api() {

        givenRestWithAuth()
                .when()
                .get("/students")
                .then()
                .statusCode(200)
                .body("data.size()", Matchers.is(2))
                .body("data[0].id", Matchers.equalTo(1))
                .body("data[0].name", Matchers.equalTo("student1"))
                .body("data[0].grade", Matchers.equalTo(1))
                .body("data[0].classroom", Matchers.equalTo(1));

    }
}
