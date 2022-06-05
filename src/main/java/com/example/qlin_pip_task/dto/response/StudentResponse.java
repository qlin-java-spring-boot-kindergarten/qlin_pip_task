package com.example.qlin_pip_task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class StudentResponse {

    private List<Student> data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity(name = "student")
    public static class Student {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_id_generator")
        @SequenceGenerator(name = "student_id_generator", sequenceName = "student_id_seq", allocationSize = 1)
        private Integer id;
        private String name;
        private Integer grade;
        private Integer classroom;
    }
}
