package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.mapper.StudentMapper;
import com.example.qlin_pip_task.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentsServiceTest {

    @Mock
    private StudentResponses studentResponses;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentsService studentsService;

//    @Test
//    void should_return_two_students_when_there_are_two_students_in_the_table(){
//        when(studentResponses.setData()).thenReturn();
//    }

    @Test
    void should_save_student_and_get_id_when_receive_student_submit_request(){
        StudentSubmitRequest studentSubmitRequest = StudentSubmitRequest.builder().name("student1").classroom(1).grade(1).build();
        StudentEntity studentEntity = StudentEntity.builder().name("student1").classroom(1).grade(1).build();
        when(studentMapper.requestToStudentEntity(studentSubmitRequest)).thenReturn(studentEntity);
        when(studentRepository.save(studentEntity))
                .thenReturn(StudentEntity.builder()
                        .id(1)
                        .name("student1")
                        .classroom(1)
                        .grade(1).build());

        Integer id = studentsService.save(studentSubmitRequest);

        assertThat(id).isEqualTo(1);
        verify(studentRepository).save(studentEntity);
    }


}