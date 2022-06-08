package com.example.qlin_pip_task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "student")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_id_generator")
    @SequenceGenerator(name = "student_id_generator", sequenceName = "student_id_seq", allocationSize = 1)
    private Integer id;
    private String name;
    private Integer grade;
    private Integer classroom;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OneToMany(mappedBy="students", cascade= CascadeType.ALL)
    private List<HomeworkEntity> homework;
}
