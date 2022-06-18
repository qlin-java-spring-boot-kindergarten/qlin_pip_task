package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.entity.ClassEntity;
import com.example.qlin_pip_task.exception.ClassNotExistsException;
import com.example.qlin_pip_task.repository.ClassRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidParameterException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;

    @InjectMocks
    private ClassService classService;

    @Test
    void should_throw_classroom_invalid_exception_when_classroom_is_smaller_than_one() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> classService.checkIfClassroomIsValid(0));
        assertTrue(exception.getMessage().contains("Classroom is invalid."));
    }

    @Test
    void should_throw_classroom_invalid_exception_when_classroom_is_larger_than_twenty() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> classService.checkIfClassroomIsValid(21));
        assertTrue(exception.getMessage().contains("Classroom is invalid."));
    }

    @Test
    void should_throw_grade_invalid_exception_when_grade_is_larger_than_nine() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> classService.checkIfGradeIsValid(10));
        assertTrue(exception.getMessage().contains("Grade is invalid."));
    }

    @Test
    void should_throw_grade_invalid_exception_when_grade_is_smaller_than_one() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> classService.checkIfGradeIsValid(0));
        assertTrue(exception.getMessage().contains("Grade is invalid."));
    }

    @Test
    void should_throw_class_not_exists_exception_when_it_does_not_exist_in_the_table() {

        when(classRepository.findByGradeAndClassroom(1, 9)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ClassNotExistsException.class, () -> classService.getClassId(1, 9));

        assertTrue(exception.getMessage().contains("The class does not exist."));

    }

    @Test
    void should_return_the_class_id_when_the_class_entity_exists() {

        when(classRepository.findByGradeAndClassroom(1, 9))
                .thenReturn(Optional.ofNullable(ClassEntity.builder().id(2).grade(1).classroom(9).build()));

        Integer result = classService.getClassId(1, 9);

        assertThat(result, is(2));

    }

}