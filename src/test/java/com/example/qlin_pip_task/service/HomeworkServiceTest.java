package com.example.qlin_pip_task.service;


import com.example.qlin_pip_task.dto.request.HomeworkSubmitRequest;
import com.example.qlin_pip_task.dto.response.HomeworkIdResponse;
import com.example.qlin_pip_task.entity.HomeworkEntity;
import com.example.qlin_pip_task.entity.TeacherEntity;
import com.example.qlin_pip_task.exception.DescriptionInvalidException;
import com.example.qlin_pip_task.mapper.HomeworkMapper;
import com.example.qlin_pip_task.repository.HomeworkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private HomeworkService homeworkService;


    @Test
    void should_return_homeworkId_when_create_new_homework_successfully() {
        HomeworkSubmitRequest homeworkSubmitRequest = HomeworkSubmitRequest.builder().teacherId(1).description("a homework").build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).build();
        when(teacherService.getNonNullTeacherEntity(1)).thenReturn(teacherEntity);
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherEntity(teacherEntity).description("a homework").build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest)).thenReturn(homeworkEntity);
        HomeworkEntity savedHomeworkEntity = HomeworkEntity.builder().id(99).teacherEntity(teacherEntity).description("a homework").build();
        when(homeworkRepository.save(homeworkEntity)).thenReturn(savedHomeworkEntity);

        HomeworkIdResponse result = homeworkService.createHomework(homeworkSubmitRequest);
        assertThat(result.getId(), is(99));
    }

    @Test
    void should_throw_description_invalid_exception_given_null_description() {
        HomeworkSubmitRequest homeworkSubmitRequest = HomeworkSubmitRequest.builder().teacherId(1).description(null).build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).build();
        when(teacherService.getNonNullTeacherEntity(1)).thenReturn(teacherEntity);
        HomeworkEntity homeworkEntity = HomeworkEntity.builder().teacherEntity(teacherEntity).description("a homework").build();
        when(homeworkMapper.homeworkRequestToEntity(homeworkSubmitRequest)).thenReturn(homeworkEntity);
        Exception exception = assertThrows(DescriptionInvalidException.class, () -> homeworkService.createHomework(homeworkSubmitRequest));
        assertThat(exception.getMessage(), is("Description is null"));
    }

}