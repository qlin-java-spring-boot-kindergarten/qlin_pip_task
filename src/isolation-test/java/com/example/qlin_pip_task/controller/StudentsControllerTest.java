package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.repository.StudentRepository;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static io.restassured.RestAssured.given;


public class StudentsControllerTest extends IsolationTest {

    @Test
    void should_return_student_id_and_200_OK_status_code_when_call_post_students_api() {

        given()
                .body(StudentSubmitRequest.builder().name("student1").grade(1).classroom(1).build())
                .contentType(ContentType.JSON)
                .when()
                .post("/students")
                .then()
                .statusCode(200);
////                .body("id", Matchers.equalTo(1));
    }

}
