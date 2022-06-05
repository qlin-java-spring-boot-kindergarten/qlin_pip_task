package com.example.qlin_pip_task.repository;

import com.example.qlin_pip_task.dto.StudentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentRepositoryTest {

    @Mock
    private StudentRepository studentRepository;

    private StudentResponse.Student student;

    @Test
    void should_return_student_object_given_find_by_id() {
        StudentResponse.Student student = StudentResponse.Student.builder()
                .name("name")
                .id(1)
                .classroom(1)
                .grade(1)
                .build();
        studentRepository.save(student);
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        StudentResponse.Student theStudent = studentRepository.findById(student.getId()).get();

        assertThat(theStudent).isNotNull();
        assertThat(theStudent.getName()).isEqualTo("name");

    }
}