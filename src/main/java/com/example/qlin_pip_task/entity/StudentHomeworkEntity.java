package com.example.qlin_pip_task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Date;
import java.time.LocalDateTime;


@Getter
@Setter
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
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, updatable = false)
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity studentEntity;

}
