package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class StudentsControllerTest extends IsolationTest {

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

    @Test
    void should_return_student_data_and_200_OK_status_code_when_call_get_students_id_api() {

        givenRestWithAuth()
                .when()
                .get("/students/1")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(1))
                .body("name", Matchers.equalTo("student1"))
                .body("grade", Matchers.equalTo(1))
                .body("classroom", Matchers.equalTo(1));

    }

    @Test
    void should_return_student_id_and_200_OK_status_code_when_call_post_students_api() {

        given()
                .body(StudentSubmitRequest.builder().name("student1").grade(1).classroom(1).build())
                .contentType(ContentType.JSON)
                .when()
                .post("/students")
                .then()
                .body("name", )
                .statusCode(200);
//                .body("id", Matchers.equalTo(1));
//                .body("name", Matchers.equalTo("student1"))
//                .body("grade", Matchers.equalTo(1))
//                .body("classroom", Matchers.equalTo(1));

    }
}
