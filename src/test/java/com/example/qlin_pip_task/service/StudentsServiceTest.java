package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkGroupResponses;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.exception.HomeworkAlreadyExistedException;
import com.example.qlin_pip_task.exception.StudentNotFoundException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.mapper.StudentMapper;
import com.example.qlin_pip_task.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentsServiceTest {

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private HomeworkMapper homeworkMapper;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentsService studentsService;

    @Test
    void should_return_two_students_when_there_are_two_students_in_the_table() {
        StudentEntity studentEntity1 = StudentEntity.builder().name("student1").classroom(1).grade(1).id(1).build();
        StudentEntity studentEntity2 = StudentEntity.builder().name("student2").classroom(2).grade(2).id(2).build();
        when(studentRepository.findAll()).thenReturn(List.of(studentEntity1, studentEntity2));
        StudentResponses.StudentResponse studentResponse1 = StudentResponses.StudentResponse.builder().name("student1").classroom(1).grade(1).id(1).build();
        StudentResponses.StudentResponse studentResponse2 = StudentResponses.StudentResponse.builder().name("student2").classroom(2).grade(2).id(2).build();
        when(studentMapper.entityToStudentResponse(studentEntity1)).thenReturn(studentResponse1);
        when(studentMapper.entityToStudentResponse(studentEntity2)).thenReturn(studentResponse2);

        StudentResponses result = studentsService.getAllStudentsResponse();

        assertThat(result.getData().get(0).getId(), is(1));
        assertThat(result.getData().get(0).getName(), is("student1"));
        assertThat(result.getData().get(0).getGrade(), is(1));
        assertThat(result.getData().get(0).getClassroom(), is(1));
        assertThat(result.getData().get(1).getId(), is(2));
        assertThat(result.getData().get(1).getName(), is("student2"));
        assertThat(result.getData().get(1).getGrade(), is(2));
        assertThat(result.getData().get(1).getClassroom(), is(2));
    }

    @Test
    void should_save_student_and_get_id_when_receive_student_submit_request() {
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

        assertThat(id, is(1));
        verify(studentRepository).save(studentEntity);
    }

    @Test
    void should_throw_invalid_parameter_exception_when_id_is_not_found() {
        when(studentRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(InvalidParameterException.class, () -> studentsService.getTheStudentResponse(anyInt()));
        assertTrue(exception.getMessage().contains("Data is not found."));
    }

    @Test
    void should_get_stduent_response_when_id_is_valid() {
        StudentEntity studentEntity = StudentEntity.builder().id(2).name("student2").classroom(2).grade(2).build();
        when(studentRepository.findById(2)).thenReturn(Optional.of(studentEntity));
        StudentResponses.StudentResponse studentResponse = StudentResponses.StudentResponse.builder().id(2).name("student2").classroom(2).grade(2).build();
        when(studentMapper.entityToStudentResponse(studentEntity)).thenReturn(studentResponse);

        StudentResponses.StudentResponse theStudentResponse = studentsService.getTheStudentResponse(2);

        assertThat(theStudentResponse.getName(), is("student2"));
        assertThat(theStudentResponse.getId(), is(2));
        assertThat(theStudentResponse.getClassroom(), is(2));
        assertThat(theStudentResponse.getGrade(), is(2));
    }

//    @Test
//    void should_get_student_response_when_name_can_be_found_in_the_table() {
//        StudentEntity studentEntity = StudentEntity.builder().id(1).name("student1").classroom(1).grade(1).build();
//        StudentResponses.StudentResponse studentResponse = StudentResponses.StudentResponse.builder().name("student1").classroom(1).grade(1).id(1).build();
//        when(studentRepository.findByName("student1")).thenReturn(studentEntity);
//        when(studentMapper.entityToStudentResponse(studentEntity)).thenReturn(studentResponse);
//
//        StudentResponses.StudentResponse theStudentResponseByName = studentsService.getTheStudentResponseByName("student1");
//
//        assertThat(theStudentResponseByName.getName(), is("student1"));
//        assertThat(theStudentResponseByName.getId(), is(1));
//        assertThat(theStudentResponseByName.getClassroom(), is(1));
//        assertThat(theStudentResponseByName.getGrade(), is(1));
//    }

//    @Test
//    void should_get_empty_response_when_name_not_in_the_table() {
//        StudentEntity studentEntity = StudentEntity.builder().build();
//        when(studentRepository.findByName("student1")).thenReturn(studentEntity);
//        when(studentMapper.entityToStudentResponse(studentEntity)).thenReturn(null);
//
//        StudentResponses.StudentResponse theStudentResponseByName = studentsService.getTheStudentResponseByName("student1");
//
//        assertThat(theStudentResponseByName, is(nullValue()));
//    }


    @Test
    void should_throw_student_not_found_exception_when_student_not_exsits() {
        when(studentRepository.existsById(1)).thenReturn(false);
        Exception exception = assertThrows(StudentNotFoundException.class, () -> studentsService.submitStudentHomework(1, HomeworkSubmitRequest.builder().build()));
        assertTrue(exception.getMessage().contains("Student not found."));
    }


    @Test
    void should_throw_homework_already_existed_exception_when_the_student_has_the_same_homework_type_existed_in_the_table() {
        when(studentRepository.existsById(1)).thenReturn(true);
        when(studentRepository.findById(1))
                .thenReturn(Optional.of(
                        StudentEntity.builder().id(1)
                                .homework(List.of(
                                        HomeworkEntity.builder().id(99).homeworkType(1).content("test").build())).build()));
        Exception exception = assertThrows(HomeworkAlreadyExistedException.class, () -> studentsService.submitStudentHomework(1, HomeworkSubmitRequest.builder().homeworkType(1).content("test").build()));
        assertTrue(exception.getMessage().contains("The homework already existed."));
    }

    @Test
    void should_save_content_and_return_student_id_and_when_receive_homework_content() {
        when(studentRepository.existsById(1)).thenReturn(true);
        HomeworkSubmitRequest homeworkSubmitRequest = HomeworkSubmitRequest.builder().build();
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().id(99).content("test").build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest)).thenReturn(homeworkEntity);
        StudentEntity studentEntity = StudentEntity.builder().id(1).homework(new ArrayList<>()).build();
        when(studentRepository.findById(1)).thenReturn(Optional.of(studentEntity));
        when(studentRepository.save(studentEntity)).thenReturn(studentEntity);

        Integer id = studentsService.submitStudentHomework(1, HomeworkSubmitRequest.builder().build());

        assertThat(id, is(99));
    }


    @Test
    void should_return_a_group_list_of_students_grouped_by_student_id_when_call_get_homework_group(){
        when(studentRepository.getHomeworkGroupList()).thenReturn(List.of(List.of("1","3"), List.of("2")));

        when(studentRepository.findById(1)).thenReturn(Optional.of(StudentEntity.builder().id(1).name("student1").build()));
        when(studentRepository.findById(3)).thenReturn(Optional.of(StudentEntity.builder().id(3).name("student3").build()));
        when(studentRepository.findById(2)).thenReturn(Optional.of(StudentEntity.builder().id(2).name("student2").build()));

        HomeworkGroupResponses homeworkGroupResponses = studentsService.getHomeworkGroup();
        assertThat(homeworkGroupResponses,
                is(HomeworkGroupResponses.builder().group(List.of(List.of("student1", "student3"), List.of("student2"))).build()));
    }
}
