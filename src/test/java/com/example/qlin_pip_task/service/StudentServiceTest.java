package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.StudentHomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentGroupsByHomeworkTypeResponses;
import com.example.qlin_pip_task.dto.response.StudentIdResponse;
import com.example.qlin_pip_task.dto.response.StudentResponses;
import com.example.qlin_pip_task.entity.ClassEntity;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
import com.example.qlin_pip_task.exception.ContentInvalidException;
import com.example.qlin_pip_task.exception.HomeworkIdInvalidException;
import com.example.qlin_pip_task.exception.StudentHomeworkAlreadyExistedException;
import com.example.qlin_pip_task.exception.StudentInvalidException;
import com.example.qlin_pip_task.exception.StudentNotFoundException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.mapper.StudentMapper;
import com.example.qlin_pip_task.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentMapper studentMapper;

    @Mock
    private HomeworkMapper homeworkMapper;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ClassService classService;

    @InjectMocks
    private StudentService studentService;

    @Test
    void should_return_two_students_when_there_are_two_students_in_the_table() {
        StudentEntity studentEntity1 = StudentEntity.builder().name("student1").id(1).classId(1).build();
        StudentEntity studentEntity2 = StudentEntity.builder().name("student2").id(2).classId(2).build();
        ClassEntity classEntity1 = ClassEntity.builder().grade(1).classroom(1).build();
        ClassEntity classEntity2 = ClassEntity.builder().grade(2).classroom(2).build();
        when(studentRepository.findAll()).thenReturn(List.of(studentEntity1, studentEntity2));
        StudentResponses.StudentResponse studentResponse1 = StudentResponses.StudentResponse.builder().name("student1").classroom(1).grade(1).id(1).build();
        StudentResponses.StudentResponse studentResponse2 = StudentResponses.StudentResponse.builder().name("student2").classroom(2).grade(2).id(2).build();
        when(classService.getClassEntityById(studentEntity1.getClassId())).thenReturn(classEntity1);
        when(classService.getClassEntityById(studentEntity2.getClassId())).thenReturn(classEntity2);
        when(studentMapper.entityToStudentResponse(studentEntity1, classEntity1)).thenReturn(studentResponse1);
        when(studentMapper.entityToStudentResponse(studentEntity2, classEntity2)).thenReturn(studentResponse2);

        StudentResponses result = studentService.getStudents();

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
        when(classService.getClassId(1, 1)).thenReturn(2);
        when(studentRepository.save(StudentEntity.builder().name("student1").classId(2).build()))
                .thenReturn(StudentEntity.builder().id(99).name("student1").classId(2).build());

        StudentIdResponse studentIdResponse = studentService.createStudent(StudentSubmitRequest.builder().name("student1").classroom(1).grade(1).build());

        assertThat(studentIdResponse.getId(), is(99));
    }

    @Test
    void should_throw_invalid_parameter_exception_when_id_is_not_found() {
        when(studentRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(StudentNotFoundException.class,
                () -> studentService.getStudentById(1));
        assertThat(exception.getMessage(), is("Student is not found."));
    }

    @Test
    void should_throw_name_invalid_exception_when_name_is_null() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> studentService.validateStudentData(
                StudentSubmitRequest.builder().name(null).classroom(1).grade(1).build()));
        assertThat(exception.getMessage(), is("Name is invalid."));
    }

    @Test
    void should_throw_name_invalid_exception_when_name_is_empty() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> studentService.validateStudentData(
                StudentSubmitRequest.builder().name("").classroom(1).grade(1).build()));
        assertThat(exception.getMessage(), is("Name is invalid."));
    }

    @Test
    void should_get_student_response_when_id_is_valid() {
        StudentEntity studentEntity = StudentEntity.builder().id(2).name("student2").classId(2).build();
        when(studentRepository.findById(2)).thenReturn(Optional.of(studentEntity));
        ClassEntity classEntity = ClassEntity.builder().grade(2).classroom(2).build();
        when(classService.getClassEntityById(studentEntity.getClassId())).thenReturn(classEntity);
        StudentResponses.StudentResponse studentResponse =
                StudentResponses.StudentResponse.builder().id(2).name("student2").classroom(2).grade(2).build();

        when(studentMapper.entityToStudentResponse(studentEntity, classEntity)).thenReturn(studentResponse);

        StudentResponses.StudentResponse theStudentResponse = studentService.getStudentById(2);

        assertThat(theStudentResponse.getName(), is("student2"));
        assertThat(theStudentResponse.getId(), is(2));
        assertThat(theStudentResponse.getClassroom(), is(2));
        assertThat(theStudentResponse.getGrade(), is(2));
    }

    @Test
    void should_get_student_responses_when_name_can_be_found_in_the_table() {
        StudentEntity studentEntity1 = StudentEntity.builder().name("student1").id(1).classId(1).build();
        StudentEntity studentEntity2 = StudentEntity.builder().name("student2").id(2).classId(2).build();
        ClassEntity classEntity1 = ClassEntity.builder().grade(1).classroom(1).build();
        ClassEntity classEntity2 = ClassEntity.builder().grade(2).classroom(2).build();
        StudentResponses.StudentResponse studentResponse1 =
                StudentResponses.StudentResponse.builder().name("student").classroom(1).grade(1).id(1).build();
        StudentResponses.StudentResponse studentResponse2 =
                StudentResponses.StudentResponse.builder().name("student").classroom(2).grade(2).id(2).build();
        when(studentRepository.findAllByName("student")).thenReturn(List.of(studentEntity1, studentEntity2));
        when(classService.getClassEntityById(studentEntity1.getClassId())).thenReturn(classEntity1);
        when(classService.getClassEntityById(studentEntity2.getClassId())).thenReturn(classEntity2);
        when(studentMapper.entityToStudentResponse(studentEntity1, classEntity1)).thenReturn(studentResponse1);
        when(studentMapper.entityToStudentResponse(studentEntity2, classEntity2)).thenReturn(studentResponse2);

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("name", "student");
        StudentResponses theStudentResponsesByName = studentService.getStudentByName(queryMap);

        assertThat(theStudentResponsesByName.getData().get(0).getName(), is("student"));
        assertThat(theStudentResponsesByName.getData().get(0).getId(), is(1));
        assertThat(theStudentResponsesByName.getData().get(1).getName(), is("student"));
        assertThat(theStudentResponsesByName.getData().get(1).getId(), is(2));
    }

    @Test
    void should_get_empty_response_when_name_not_in_the_table() {
        when(studentRepository.findAllByName("student")).thenReturn(List.of());

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("name", "student");
        StudentResponses theStudentResponseByName = studentService.getStudentByName(queryMap);

        assertThat(theStudentResponseByName.getData(), is(List.of()));
    }


    @Test
    void should_throw_student_not_found_exception_when_student_not_exists() {
        when(studentRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(StudentNotFoundException.class,
                () -> studentService.createStudentHomework(1, StudentHomeworkSubmitRequest.builder().build()));
        assertThat(exception.getMessage(), is("Student is not found."));
    }


    @Test
    void should_throw_homework_already_existed_exception_when_the_student_has_the_same_homework_type_existed_in_the_table() {
        when(studentRepository.findById(1)).thenReturn(Optional.empty());
        when(studentRepository.findById(1))
                .thenReturn(Optional.of(
                        StudentEntity.builder().id(1)
                                .studentHomework(List.of(
                                        StudentHomeworkEntity.builder().id(99)
                                                .homeworkEntity(HomeworkEntity.builder().id(1).build()).content("test")
                                                .build())).build()));
        Exception exception = assertThrows(StudentHomeworkAlreadyExistedException.class,
                () -> studentService.createStudentHomework(1, StudentHomeworkSubmitRequest.builder().homeworkId(1).content("test").build()));
        assertTrue(exception.getMessage().contains("The homework already existed."));
    }

    @Test
    void should_save_content_and_return_student_id_and_when_receive_homework_content() {
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().id(1).build();
        when(homeworkMapper.studentHomeworkRequestToEntity(StudentHomeworkSubmitRequest.builder().content("test").homeworkId(1).build()))
                .thenReturn(StudentHomeworkEntity.builder().homeworkEntity(homeworkEntity).content("test").build());
        StudentEntity studentEntity = StudentEntity.builder().id(1).studentHomework(new ArrayList<>()).build();
        when(studentRepository.findById(1)).thenReturn(Optional.of(studentEntity));
        StudentHomeworkEntity studentHomeworkEntity = StudentHomeworkEntity.builder().id(99).content("test").id(6).build();
        when(studentRepository.save(studentEntity)).thenReturn(
                StudentEntity.builder().id(1).studentHomework(List.of(studentHomeworkEntity)).build());
        when(homeworkMapper.homeworkEntityToHomeworkIdResponse(studentHomeworkEntity))
                .thenReturn(HomeworkIdResponse.builder().id(99).build());

        HomeworkIdResponse homeworkIdResponse =
                studentService.createStudentHomework(1, StudentHomeworkSubmitRequest.builder().content("test").homeworkId(1).build());

        assertThat(homeworkIdResponse.getId(), is(99));
    }

    @Test
    void should_get_student_groups_given_homework_types() {
        StudentEntity studentEntity1 = StudentEntity.builder().id(1).name("student1").classId(1)
                .studentHomework(List.of(
                        StudentHomeworkEntity.builder().homeworkEntity(HomeworkEntity.builder().id(1).build()).build(),
                        StudentHomeworkEntity.builder().homeworkEntity(HomeworkEntity.builder().id(3).build()).build()
                )).build();
        StudentEntity studentEntity2 = StudentEntity.builder().id(2).name("student2").classId(2)
                .studentHomework(List.of(
                        StudentHomeworkEntity.builder().homeworkEntity(HomeworkEntity.builder().id(2).build()).build()
                )).build();
        StudentEntity studentEntity3 = StudentEntity.builder().id(3).name("student3").classId(3)
                .studentHomework(List.of(
                        StudentHomeworkEntity.builder().homeworkEntity(HomeworkEntity.builder().id(1).build()).build(),
                        StudentHomeworkEntity.builder().homeworkEntity(HomeworkEntity.builder().id(2).build()).build(),
                        StudentHomeworkEntity.builder().homeworkEntity(HomeworkEntity.builder().id(3).build()).build()
                )).build();
        ClassEntity classEntity1 = ClassEntity.builder().grade(1).classroom(1).build();
        ClassEntity classEntity2 = ClassEntity.builder().grade(2).classroom(2).build();
        ClassEntity classEntity3 = ClassEntity.builder().grade(3).classroom(3).build();
        when(classService.getClassEntityById(studentEntity1.getClassId())).thenReturn(classEntity1);
        when(classService.getClassEntityById(studentEntity2.getClassId())).thenReturn(classEntity2);
        when(classService.getClassEntityById(studentEntity3.getClassId())).thenReturn(classEntity3);
        when(studentRepository.findAll()).thenReturn(List.of(studentEntity1, studentEntity2, studentEntity3));
        when(studentMapper.entityToStudentResponse(studentEntity1, classEntity1))
                .thenReturn(StudentResponses.StudentResponse.builder().id(1).name("student1").build());
        when(studentMapper.entityToStudentResponse(studentEntity2, classEntity2))
                .thenReturn(StudentResponses.StudentResponse.builder().id(2).name("student2").build());
        when(studentMapper.entityToStudentResponse(studentEntity3, classEntity3))
                .thenReturn(StudentResponses.StudentResponse.builder().id(3).name("student3").build());

        StudentGroupsByHomeworkTypeResponses result = studentService.getStudentGroupsByHomework();
        Map<String, List<String>> resultGroup = result.getHomework();
        assertThat(resultGroup.size(), is(3));
        assertThat(resultGroup.get("homework_type_1"), is(List.of("student1", "student3")));
        assertThat(resultGroup.get("homework_type_2"), is(List.of("student2", "student3")));
        assertThat(resultGroup.get("homework_type_3"), is(List.of("student1", "student3")));
    }

    @Test
    void should_throw_content_invalid_exception_when_submit_update_homework_request_with_no_content_provided() {
        StudentEntity studentEntity1 = StudentEntity.builder().id(1).name("student1")
                .studentHomework(List.of(StudentHomeworkEntity.builder().homeworkEntity(HomeworkEntity.builder().id(1).build()).build())).build();
        when(studentRepository.findById(1)).thenReturn(Optional.of(studentEntity1));
        Exception exception = assertThrows(ContentInvalidException.class,
                () -> studentService.updateStudentHomework(1, StudentHomeworkSubmitRequest.builder().homeworkId(1).build()));
        assertTrue(exception.getMessage().contains("Homework content is invalid."));
    }

    @Test
    void should_throw_content_invalid_exception_when_submit_update_homework_request_with_content_is_empty() {
        StudentEntity studentEntity1 = StudentEntity.builder().id(1).name("student1")
                .studentHomework(List.of(StudentHomeworkEntity.builder().homeworkEntity(HomeworkEntity.builder().id(1).build()).build())).build();
        when(studentRepository.findById(1)).thenReturn(Optional.of(studentEntity1));
        Exception exception = assertThrows(ContentInvalidException.class,
                () -> studentService.updateStudentHomework(1, StudentHomeworkSubmitRequest.builder().content("").homeworkId(1).build()));
        assertTrue(exception.getMessage().contains("Homework content is invalid."));
    }

    @Test
    void should_throw_homework_type_not_exist_exception_given_homework_type_not_existed() {
        StudentEntity studentEntity1 = StudentEntity.builder().id(1).name("student1")
                .studentHomework(List.of(
                        StudentHomeworkEntity.builder().homeworkEntity(HomeworkEntity.builder().id(1).build()).build(),
                        StudentHomeworkEntity.builder().homeworkEntity(HomeworkEntity.builder().id(3).build()).build()
                )).build();
        StudentEntity studentEntity2 = StudentEntity.builder().id(2).name("student2")
                .studentHomework(List.of(
                        StudentHomeworkEntity.builder().homeworkEntity(HomeworkEntity.builder().id(2).build()).build()
                )).build();
        when(studentRepository.findById(1)).thenReturn(Optional.of(studentEntity1));
        when(studentRepository.findAll()).thenReturn(List.of(studentEntity1, studentEntity2));

        Exception exception = assertThrows(HomeworkIdInvalidException.class,
                () -> studentService.updateStudentHomework(1, StudentHomeworkSubmitRequest.builder().content("test_content").homeworkId(99).build()));

        assertTrue(exception.getMessage().contains("Homework id is invalid."));
    }

    @Test
    void should_update_homework_content_when_valid_homework_update_request() {
        StudentEntity studentEntity1 = StudentEntity.builder().id(1).name("student1")
                .studentHomework(List.of(
                        StudentHomeworkEntity.builder().homeworkEntity(HomeworkEntity.builder().id(1).build()).content("content").build(),
                        StudentHomeworkEntity.builder().homeworkEntity(HomeworkEntity.builder().id(3).build()).build()
                )).build();
        when(studentRepository.findAll()).thenReturn(List.of(studentEntity1));
        when(studentRepository.findById(1)).thenReturn(Optional.of(studentEntity1));

        StudentHomeworkSubmitRequest request = StudentHomeworkSubmitRequest.builder().content("update_content").homeworkId(1).build();
        ArgumentCaptor<StudentEntity> argumentCaptor = ArgumentCaptor.forClass(StudentEntity.class);
        studentService.updateStudentHomework(1, request);

        verify(studentRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getStudentHomework().get(0).getContent(), is("update_content"));
    }

    @Test
    void should_throw_student_invalid_exception_given_null_student_id() {
        Exception exception = assertThrows(StudentInvalidException.class,
                () -> studentService.getNotNullStudentEntity(null));
        assertThat(exception.getMessage(), is("Student id is null."));
    }

}
