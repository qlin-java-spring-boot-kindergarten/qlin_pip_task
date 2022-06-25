package com.example.qlin_pip_task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "homework")
public class HomeworkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "homework_id_generator")
    @SequenceGenerator(name = "homework_id_generator", sequenceName = "homework_id_seq", allocationSize = 1)
    private Integer id;
    private String description;

    private Integer classId;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacherEntity;
}
