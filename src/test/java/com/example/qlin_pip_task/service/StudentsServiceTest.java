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

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


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

    @Test
    void should_return_two_students_when_there_are_two_students_in_the_table(){
        StudentEntity studentEntity1 = StudentEntity.builder().name("student1").classroom(1).grade(1).build();
        StudentEntity studentEntity2 = StudentEntity.builder().name("student2").classroom(2).grade(2).build();
        StudentResponses.StudentResponse studentResponse1 = StudentResponses.StudentResponse.builder().name("student1").classroom(1).grade(1).build();
        StudentResponses.StudentResponse studentResponse2 = StudentResponses.StudentResponse.builder().name("student2").classroom(2).grade(2).build();
        when(studentRepository.findAll()).thenReturn(List.of(studentEntity1, studentEntity2));
        when(studentMapper.entityToStudentResponse(studentEntity1)).thenReturn(studentResponse1);
        when(studentMapper.entityToStudentResponse(studentEntity2)).thenReturn(studentResponse2);

        // void
//        when(studentResponses.setData(List.of(studentResponse1, studentResponse2))).thenReturn(??);

        StudentResponses result = studentsService.getAllStudentsResponse();
        assertThat(result.getData().get(0).getName()).isEqualTo("student1");
    }

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

    @Test
    void should_throw_invalid_parameter_exception_when_id_is_not_found(){
        when(studentRepository.findById(anyInt())).thenReturn(Optional.empty());
    Exception exception = assertThrows (InvalidParameterException.class, () -> studentsService.getTheStudentResponse(anyInt()));
    assertTrue(exception.getMessage().contains("Data is not found."));
    }

    @Test
    void should_get_stduent_response_when_id_is_valid (){
        StudentEntity studentEntity = StudentEntity.builder().id(2).name("student2").classroom(2).grade(2).build();
        when(studentRepository.findById(anyInt())).thenReturn(Optional.ofNullable(studentEntity));
        Optional<StudentEntity> mockOptionalEntity = studentRepository.findById(2);
        when(mockOptionalEntity.get()).thenReturn(Optional.ofNullable(studentEntity).get());
        StudentResponses.StudentResponse response = studentsService.getTheStudentResponse(2);
        assertThat(response.getId()).isEqualTo(2);
    }
}
