package com.example.qlin_pip_task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Student")
public class Student {
    @Id
    private Integer id;
    private String name;
    private Integer grade;
    private Integer classroom;
}
