package com.example.qlin_pip_task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="student_homework")
public class StudentHomeworkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "student_homework_id_generator")
    @SequenceGenerator(name = "student_homework_id_generator", sequenceName = "student_homework_id_seq", allocationSize = 1)
    private Integer id;
    private String content;
    private Integer homeworkId;

    @CreationTimestamp
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity studentEntity;

}
