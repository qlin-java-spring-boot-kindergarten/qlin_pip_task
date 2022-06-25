package com.example.qlin_pip_task.exception;

public class TeacherNoFoundException extends CustomResourceNotFoundException {
    public TeacherNoFoundException(String message) {
        super(message);
    }
}
