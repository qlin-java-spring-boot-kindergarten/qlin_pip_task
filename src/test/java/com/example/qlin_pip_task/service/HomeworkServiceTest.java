package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.request.NewStudentHomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentHomeworkIdResponse;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
import com.example.qlin_pip_task.exception.DescriptionInvalidException;
import com.example.qlin_pip_task.exception.HomeworkIdInvalidException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.mapper.NewStudentHomeworkMapper;
import com.example.qlin_pip_task.repository.HomeworkRepository;
import com.example.qlin_pip_task.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class HomeworkServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private HomeworkMapper homeworkMapper;
    @Mock
    private HomeworkRepository homeworkRepository;
    @Mock
    private NewStudentHomeworkMapper newStudentHomeworkMapper;
    @Mock
    private StudentsService studentsService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private ClassService classService;

    @InjectMocks
    private HomeworkService homeworkService;

    @Test
    void should_throw_description_invalid_exception_when_description_is_null() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(1).description(null).build();
        Exception exception = assertThrows(DescriptionInvalidException.class, () -> homeworkService.save(request));

        assertTrue(exception.getMessage().contains("Description cannot be null."));
    }

    @Test
    void should_throw_description_invalid_exception_when_description_is_empty() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(1).description("").build();
        Exception exception = assertThrows(DescriptionInvalidException.class, () -> homeworkService.save(request));

        assertTrue(exception.getMessage().contains("Description cannot be empty."));
    }

    @Test
    void should_throw_description_invalid_exception_when_description_has_only_whitespace() {
        HomeworkSubmitRequest request = HomeworkSubmitRequest.builder().teacherId(1).description("           ").build();
        Exception exception = assertThrows(DescriptionInvalidException.class, () -> homeworkService.save(request));

        assertTrue(exception.getMessage().contains("Description cannot be empty."));
    }

    @Test
    void should_save_homework_and_return_homework_id_given_valid_teacher_id_and_class_id_and_description() {
        HomeworkSubmitRequest homeworkSubmitRequest =
                HomeworkSubmitRequest.builder().classId(1).description("test").teacherId(11).build();
        doNothing().when(teacherService).checkIfTeacherEntityExists(11);
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherId(11).description("test").classId(1).build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest))
                .thenReturn(homeworkEntity);
        HomeworkEntity savedHomeworkEntity = HomeworkEntity.builder().id(99).teacherId(11).description("test").classId(1).build();
        when(homeworkRepository.save(homeworkEntity)).thenReturn(savedHomeworkEntity);

        HomeworkIdResponse result = homeworkService.save(homeworkSubmitRequest);

        assertThat(result.getId(), is(99));
    }


    @Test
    void should_throw_homework_id_invalid_exception_when_homework_id_is_not_found() {
        NewStudentHomeworkSubmitRequest request =
                NewStudentHomeworkSubmitRequest.builder().studentId(1).content("test").build();
        when(homeworkRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(HomeworkIdInvalidException.class,
                () -> homeworkService.getStudentHomeworkIdResponse(1, request));
        assertTrue(exception.getMessage().contains("The homework id is not found."));
    }


    @Test
    void should_save_homework_and_return_homework_id_given_valid_student_id_and_class_id_and_description() {
        NewStudentHomeworkSubmitRequest newStudentHomeworkSubmitRequest =
                NewStudentHomeworkSubmitRequest.builder().studentId(1).classId(1).content("homework").build();
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().id(4).build();
        when(homeworkRepository.findById(4)).thenReturn(Optional.of(homeworkEntity));
        doNothing().when(studentsService).checkIfStudentIdIsNull(1);
        StudentHomeworkEntity studentHomeworkEntity =
                StudentHomeworkEntity.builder().homeworkId(4).classId(1).content("homework").build();
        StudentEntity studentEntity = StudentEntity.builder().id(1).name("student").classId(1)
                .studentHomework(new ArrayList<>()).build();
        when(studentsService.getNotNullableStudentEntity(1)).thenReturn(studentEntity);
        when(newStudentHomeworkMapper.homeworkRequestToEntity(newStudentHomeworkSubmitRequest)).thenReturn(studentHomeworkEntity);
        doNothing().when(classService).checkIfClassIdIsValid(1);
        doNothing().when(classService).checkIfClassIsTheSame(studentHomeworkEntity, 1);
        when(studentRepository.save(studentEntity)).thenReturn(StudentEntity.builder().id(1).name("student").classId(1)
                .studentHomework(List.of(
                        StudentHomeworkEntity.builder().homeworkId(4).id(99).content("homework").build()))
                .build());

        StudentHomeworkIdResponse result =
                homeworkService.getStudentHomeworkIdResponse(4, newStudentHomeworkSubmitRequest);

        assertThat(result.getId(), is(99));
    }
}