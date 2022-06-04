package com.example.qlin_pip_task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {

    private List<Student> data;

    @Entity(name = "student")
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Student {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        private String name;
        private Integer grade;
        private Integer classroom;
    }
}
