package com.example.qlin_pip_task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="homework")
public class HomeworkEntity {
    @Id
    private Integer id;
    private Integer studentId;
    private String content;

    @ManyToOne
    private StudentEntity studentEntity;

}
