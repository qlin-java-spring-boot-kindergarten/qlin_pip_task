package com.example.qlin_pip_task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="homework")
public class HomeworkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "homework_id_generator")
    @SequenceGenerator(name = "homework_id_generator", sequenceName = "homework_id_seq", allocationSize = 1)
    private Integer id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity studentEntity;

}
