package com.example.qlin_pip_task.service;


import com.example.qlin_pip_task.dto.request.HomeworkAnswerSubmitRequest;
import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.dto.response.StudentHomeworkGroupByIdAndDateAndClassResponses;
import com.example.qlin_pip_task.dto.response.StudentHomeworkIdResponse;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.entity.StudentHomeworkEntity;
import com.example.qlin_pip_task.entity.TeacherEntity;
import com.example.qlin_pip_task.exception.ContentInvalidException;
import com.example.qlin_pip_task.exception.DateInvalidException;
import com.example.qlin_pip_task.exception.HomeworkNotFoundException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.repository.HomeworkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HomeworkServiceTest {
    @Mock
    private HomeworkMapper homeworkMapper;
    @Mock
    private HomeworkRepository homeworkRepository;
    @Mock
    private TeacherService teacherService;
    @Mock
    private StudentService studentService;
    @Mock
    private ClassService classService;

    @InjectMocks
    private HomeworkService homeworkService;


    @Test
    void should_return_homeworkId_when_create_new_homework_successfully() {
        HomeworkSubmitRequest homeworkSubmitRequest = HomeworkSubmitRequest.builder().teacherId(1).content("a homework").build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).build();
        when(teacherService.getNotNullTeacherEntity(1)).thenReturn(teacherEntity);
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherEntity(teacherEntity).content("a homework").build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest)).thenReturn(homeworkEntity);
        HomeworkEntity savedHomeworkEntity = HomeworkEntity.builder().id(99).teacherEntity(teacherEntity).content("a homework").build();
        when(homeworkRepository.save(homeworkEntity)).thenReturn(savedHomeworkEntity);

        HomeworkIdResponse result = homeworkService.createHomework(homeworkSubmitRequest);
        assertThat(result.getId(), is(99));
    }

    @Test
    void should_throw_content_invalid_exception_given_null_content() {
        HomeworkSubmitRequest homeworkSubmitRequest = HomeworkSubmitRequest.builder().teacherId(1).content(null).build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).build();
        when(teacherService.getNotNullTeacherEntity(1)).thenReturn(teacherEntity);
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherEntity(teacherEntity).content(null).build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest)).thenReturn(homeworkEntity);
        Exception exception = assertThrows(ContentInvalidException.class, () -> homeworkService.createHomework(homeworkSubmitRequest));
        assertThat(exception.getMessage(), is("Content is null."));
    }

    @Test
    void should_throw_content_invalid_exception_given_empty_content() {
        HomeworkSubmitRequest homeworkSubmitRequest = HomeworkSubmitRequest.builder().teacherId(1).content("").build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).build();
        when(teacherService.getNotNullTeacherEntity(1)).thenReturn(teacherEntity);
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherEntity(teacherEntity).content("").build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest)).thenReturn(homeworkEntity);
        Exception exception = assertThrows(ContentInvalidException.class, () -> homeworkService.createHomework(homeworkSubmitRequest));
        assertThat(exception.getMessage(), is("Content is empty."));
    }

    @Test
    void should_throw_content_invalid_exception_given_whitespace_only_content() {
        HomeworkSubmitRequest homeworkSubmitRequest = HomeworkSubmitRequest.builder().teacherId(1).content("     ").build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).build();
        when(teacherService.getNotNullTeacherEntity(1)).thenReturn(teacherEntity);
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherEntity(teacherEntity).content("      ").build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest)).thenReturn(homeworkEntity);
        Exception exception = assertThrows(ContentInvalidException.class, () -> homeworkService.createHomework(homeworkSubmitRequest));
        assertThat(exception.getMessage(), is("Content is empty."));
    }

    @Test
    void should_throw_content_invalid_exception_given_duplicated_content() {
        HomeworkSubmitRequest homeworkSubmitRequest = HomeworkSubmitRequest.builder().teacherId(1).content("a homework").build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).build();
        when(teacherService.getNotNullTeacherEntity(1)).thenReturn(teacherEntity);
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherEntity(teacherEntity).content("a homework").build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest)).thenReturn(homeworkEntity);
        when(homeworkRepository.existsByContent("a homework")).thenReturn(true);

        Exception exception = assertThrows(ContentInvalidException.class, () -> homeworkService.createHomework(homeworkSubmitRequest));
        assertThat(exception.getMessage(), is("Content is duplicated."));
    }

    @Test
    void should_save_student_homework_and_return_its_id_given_valid_student_id_and_homework_content() {
        HomeworkAnswerSubmitRequest homeworkAnswerSubmitRequest =
                HomeworkAnswerSubmitRequest.builder().studentId(1).content("this is an answer").build();
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().id(3).studentHomework(new ArrayList<>()).build();
        when(homeworkRepository.findById(9)).thenReturn(Optional.of(homeworkEntity));
        StudentEntity studentEntity = StudentEntity.builder().id(1).name("student").classId(2).build();
        when(studentService.getNotNullStudentEntity(1)).thenReturn(studentEntity);
        StudentHomeworkEntity studentHomeworkEntity =
                StudentHomeworkEntity.builder().content("this is an answer").studentEntity(studentEntity).build();
        when(homeworkMapper.homeworkAnswerRequestToEntity(homeworkAnswerSubmitRequest)).thenReturn(studentHomeworkEntity);
        StudentHomeworkEntity savedStudentHomeworkEntity = StudentHomeworkEntity.builder().id(100)
                .studentEntity(StudentEntity.builder().id(1).classId(2).studentHomework(List.of(studentHomeworkEntity)).build()).build();
        HomeworkEntity savedHomeworkEntity = HomeworkEntity.builder().id(3).studentHomework(List.of(savedStudentHomeworkEntity)).build();
        when(homeworkRepository.save(homeworkEntity)).thenReturn(savedHomeworkEntity);

        StudentHomeworkIdResponse result = homeworkService.createStudentHomework(9, homeworkAnswerSubmitRequest);

        assertThat(result.getId(), is(100));
    }

    @Test
    void should_throw_homework_not_found_exception_when_optional_homework_entity_is_empty_given_homework_id() {
        HomeworkAnswerSubmitRequest homeworkAnswerSubmitRequest =
                HomeworkAnswerSubmitRequest.builder().content("answer").studentId(1).build();
        when(homeworkRepository.findById(9)).thenReturn(Optional.empty());
        Exception exception = assertThrows(HomeworkNotFoundException.class,
                () -> homeworkService.createStudentHomework(9, homeworkAnswerSubmitRequest));
        assertThat(exception.getMessage(), is("Homework is not found."));
    }

    @Test
    void should_throw_content_invalid_exception_given_a_student_submit_a_duplicated_student_homework_content() {
        HomeworkAnswerSubmitRequest homeworkAnswerSubmitRequest =
                HomeworkAnswerSubmitRequest.builder().content("content").studentId(1).build();
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().id(9)
                .studentHomework(List.of(
                        StudentHomeworkEntity.builder()
                                .studentEntity(
                                        StudentEntity.builder().id(1).build()).content("content").build())).build();
        when(homeworkRepository.findById(9)).thenReturn(Optional.of(homeworkEntity));
        Exception exception = assertThrows(ContentInvalidException.class,
                () -> homeworkService.createStudentHomework(9, homeworkAnswerSubmitRequest));
        assertThat(exception.getMessage(), is("Content is duplicated."));
    }


    @Test
    void should_return_grouped_student_homework_by_date_and_class_id_and_homework_id() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("homework_id", "1");
        queryMap.put("grade", "2");
        queryMap.put("classroom", "3");
        queryMap.put("created_at", "2022-06-25");
        when(classService.getValidClassId("2", "3")).thenReturn(4);
        when(homeworkRepository.existsByStudentHomeworkCreatedAt(LocalDate.parse("2022-06-25"))).thenReturn(true);
        StudentHomeworkEntity studentHomeworkEntity = StudentHomeworkEntity.builder()
                .createdAt(LocalDate.parse("2022-06-25"))
                .classId(4)
                .content("homework content")
                .studentEntity(StudentEntity.builder().id(99).build()).build();
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().id(1).studentHomework(List.of(studentHomeworkEntity)).build();
        when(homeworkRepository.findById(1)).thenReturn(Optional.of(homeworkEntity));
        StudentHomeworkGroupByIdAndDateAndClassResponses.StudentHomeworkResponse studentHomeworkResponse =
                StudentHomeworkGroupByIdAndDateAndClassResponses.StudentHomeworkResponse.builder()
                        .studentId(99)
                        .content("homework content")
                        .build();
        when(homeworkMapper.homeworkEntityToGroupedStudentHomeworkResponse(studentHomeworkEntity)).thenReturn(studentHomeworkResponse);

        StudentHomeworkGroupByIdAndDateAndClassResponses studentHomeworkByHomeworkIdAndClassIdAndDate =
                homeworkService.getStudentHomeworkByHomeworkIdAndClassIdAndDate(queryMap);

        assertThat(studentHomeworkByHomeworkIdAndClassIdAndDate.getHomeworkId(), is(1));
        assertThat(studentHomeworkByHomeworkIdAndClassIdAndDate.getGrade(), is(2));
        assertThat(studentHomeworkByHomeworkIdAndClassIdAndDate.getClassroom(), is(3));
        assertThat(studentHomeworkByHomeworkIdAndClassIdAndDate.getCreatedAt(), is("2022-06-25"));
    }

    @Test
    void should_throw_date_invalid_exception_given_null_date() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("homework_id", "1");
        queryMap.put("grade", "2");
        queryMap.put("classroom", "3");
        queryMap.put("created_at", null);
        when(classService.getValidClassId("2", "3")).thenReturn(4);
        Exception exception = assertThrows(DateInvalidException.class, () -> homeworkService.getStudentHomeworkByHomeworkIdAndClassIdAndDate(queryMap));
        assertThat(exception.getMessage(), is("Date is null."));
    }

    @Test
    void should_throw_date_invalid_exception_given_non_date_string() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("homework_id", "1");
        queryMap.put("grade", "2");
        queryMap.put("classroom", "3");
        queryMap.put("created_at", "date");
        when(classService.getValidClassId("2", "3")).thenReturn(4);
        Exception exception = assertThrows(DateInvalidException.class, () -> homeworkService.getStudentHomeworkByHomeworkIdAndClassIdAndDate(queryMap));
        assertThat(exception.getMessage(), is("Date is invalid."));
    }

    @Test
    void should_throw_date_invalid_exception_given_not_existing_date() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("homework_id", "1");
        queryMap.put("grade", "2");
        queryMap.put("classroom", "3");
        queryMap.put("created_at", "2022-06-25");
        when(classService.getValidClassId("2", "3")).thenReturn(4);
        when(homeworkRepository.existsByStudentHomeworkCreatedAt(LocalDate.parse("2022-06-25"))).thenReturn(false);
        Exception exception = assertThrows(DateInvalidException.class, () -> homeworkService.getStudentHomeworkByHomeworkIdAndClassIdAndDate(queryMap));
        assertThat(exception.getMessage(), is("Date is invalid."));
    }

}