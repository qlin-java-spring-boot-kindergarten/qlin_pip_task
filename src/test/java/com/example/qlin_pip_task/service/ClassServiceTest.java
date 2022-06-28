package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.entity.ClassEntity;
import com.example.qlin_pip_task.exception.ClassNotFoundException;
import com.example.qlin_pip_task.exception.ClassroomInvalidException;
import com.example.qlin_pip_task.exception.GradeInvalidException;
import com.example.qlin_pip_task.repository.ClassRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;

    @InjectMocks
    private ClassService classService;

    @Test
    void should_throw_classroom_invalid_exception_when_classroom_is_smaller_than_one() {
        Exception exception = assertThrows(ClassroomInvalidException.class, () -> classService.checkIfClassroomIsValid(0));
        assertThat(exception.getMessage(), is("Classroom is invalid."));
    }

    @Test
    void should_throw_classroom_invalid_exception_when_classroom_is_larger_than_twenty() {
        Exception exception = assertThrows(ClassroomInvalidException.class, () -> classService.checkIfClassroomIsValid(21));
        assertThat(exception.getMessage(), is("Classroom is invalid."));
    }

    @Test
    void should_throw_grade_invalid_exception_when_grade_is_larger_than_nine() {
        Exception exception = assertThrows(GradeInvalidException.class, () -> classService.checkIfGradeIsValid(10));
        assertThat(exception.getMessage(), is("Grade is invalid."));
    }

    @Test
    void should_throw_grade_invalid_exception_when_grade_is_smaller_than_one() {
        Exception exception = assertThrows(GradeInvalidException.class, () -> classService.checkIfGradeIsValid(0));
        assertThat(exception.getMessage(), is("Grade is invalid."));
    }

    @Test
    void should_throw_class_not_exists_exception_when_it_does_not_exist_in_the_table() {

        when(classRepository.findByGradeAndClassroom(1, 9)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ClassNotFoundException.class, () -> classService.getClassId(1, 9));

        assertThat(exception.getMessage(), is("The class is not found."));

    }

    @Test
    void should_return_the_class_id_when_the_class_entity_exists() {
        when(classRepository.findByGradeAndClassroom(1, 9))
                .thenReturn(Optional.ofNullable(ClassEntity.builder().id(2).grade(1).classroom(9).build()));

        Integer result = classService.getClassId(1, 9);

        assertThat(result, is(2));

    }

    @Test
    void should_return_a_class_entity_given_class_id() {
        when(classRepository.findById(1))
                .thenReturn(Optional.ofNullable(ClassEntity.builder().id(1).grade(1).classroom(1).build()));

        ClassEntity result = classService.getClassEntityById(1);

        assertThat(result.getGrade(), is(1));
        assertThat(result.getClassroom(), is(1));

    }

    @Test
    void should_throw_grade_invalid_exception_given_null_grade_string() {
        Exception exception = assertThrows(GradeInvalidException.class, () -> classService.checkIfGradeStrIsValid(null));
        assertThat(exception.getMessage(), is("Grade is null."));
    }

    @Test
    void should_throw_grade_invalid_exception_given_non_numeric_grade_string() {
        Exception exception = assertThrows(GradeInvalidException.class, () -> classService.checkIfGradeStrIsValid("not a integer"));
        assertThat(exception.getMessage(), is("Grade is invalid."));
    }

    @Test
    void should_throw_classroom_invalid_exception_given_null_classroom_string() {
        Exception exception = assertThrows(ClassroomInvalidException.class, () -> classService.checkIfClassroomStrIsValid(null));
        assertThat(exception.getMessage(), is("Classroom is null."));
    }

    @Test
    void should_throw_classroom_invalid_exception_given_non_numeric_classroom_string() {
        Exception exception = assertThrows(ClassroomInvalidException.class, () -> classService.checkIfClassroomStrIsValid("int"));
        assertThat(exception.getMessage(), is("Classroom is invalid."));
    }

}
