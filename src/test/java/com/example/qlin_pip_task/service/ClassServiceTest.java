package com.example.qlin_pip_task.service;

import com.example.qlin_pip_task.entity.ClassEntity;
import com.example.qlin_pip_task.repository.ClassRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;

    @InjectMocks
    private ClassService classService;

    @Test
    void should_save_a_new_class_entity_and_return_the_new_class_id_when_it_did_not_exist_before() {

        when(classRepository.findClassEntityByGradeAndClassroom(1, 9)).thenReturn(null);

//        when(classRepository.save(ClassEntity.builder().grade(1).classroom(9).build())).thenReturn(ClassEntity.builder().id(2).grade(1).classroom(9).build());
        when(classRepository.save(any())).thenReturn(ClassEntity.builder().id(2).grade(1).classroom(9).build());

        Integer result = classService.getClassId(1, 9);

        assertThat(result, is(2));

    }

    @Test
    void should_return_the_class_id_when_the_class_entity_exists() {

        when(classRepository.findClassEntityByGradeAndClassroom(1, 9))
                .thenReturn(ClassEntity.builder().id(2).grade(1).classroom(9).build());

        Integer result = classService.getClassId(1, 9);

        assertThat(result, is(2));

    }

}