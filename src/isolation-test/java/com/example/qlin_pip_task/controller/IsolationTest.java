package com.example.qlin_pip_task.controller;

import com.example.qlin_pip_task.QlinPipTaskApplication;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

@ActiveProfiles
@SpringBootTest(classes = QlinPipTaskApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IsolationTest {

    @LocalServerPort
    private transient int port;

    @BeforeEach
    public void setDefaultsForRestAssured(){
        RestAssured.port = port;
    }

    @AfterEach
    public void resetRestAssured(){
        RestAssured.reset();
    }

    protected RequestSpecification givenRestWithAuth() {
        return given();
    }

}
