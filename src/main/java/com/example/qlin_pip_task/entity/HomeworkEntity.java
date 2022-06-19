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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Date;

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

    @JoinColumn(name = "class_id")
    private Integer class_id;

    @JoinColumn(name = "teacher_id")
    private Integer teacherId;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, updatable = false)
    private Date updatedAt;
}
