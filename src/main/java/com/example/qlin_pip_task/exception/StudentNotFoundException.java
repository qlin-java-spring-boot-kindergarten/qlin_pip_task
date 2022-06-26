package com.example.qlin_pip_task.exception;

public class StudentNotFoundException extends CustomResourceNotFoundException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
