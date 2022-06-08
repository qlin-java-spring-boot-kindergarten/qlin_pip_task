package com.example.qlin_pip_task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "homework")
public class HomeworkEntity {
    @Id
    private Integer id;
    private Integer studentId;
    private String content;

}
