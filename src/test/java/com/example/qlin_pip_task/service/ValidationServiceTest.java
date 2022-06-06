//package com.example.qlin_pip_task.service;
//
//import com.example.qlin_pip_task.dto.request.StudentSubmitRequest;
//import com.example.qlin_pip_task.entity.StudentEntity;
//import com.example.qlin_pip_task.exception.NameInvalidException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.when;
//
//
//@ExtendWith(MockitoExtension.class)
//class ValidationServiceTest {
//
//    @Mock
//    private StudentsService studentsService;
//
//    @InjectMocks
//    private ValidationService validationService;
//
//    @Test
//    void should_throw_name_invalid_exception_when_name_is_null() {
//        Exception exception = assertThrows(NameInvalidException.class, () -> validationService.validateStudentData(
//                StudentSubmitRequest.builder()
//                        .name(null)
//                        .classroom(1)
//                        .grade(1)
//                        .build()));
//        assertTrue(exception.getMessage().contains("Name is invalid."));
//    }
//
//    @Test
//    void should_throw_name_invalid_exception_when_name_is_empty() {
//        Exception exception = assertThrows(NameInvalidException.class, () -> validationService.validateStudentData(
//                StudentSubmitRequest.builder()
//                        .name("")
//                        .classroom(1)
//                        .grade(1)
//                        .build()));
//        assertTrue(exception.getMessage().contains("Name is invalid."));
//    }
//
//    @Test
//    void should_throw_grade_invalid_exception_when_grade_is_smaller_than_one() {
//        Exception exception = assertThrows(GradeInvalidException.class, () -> validationService.validateStudentData(
//                StudentSubmitRequest.builder()
//                        .name("name")
//                        .classroom(1)
//                        .grade(0)
//                        .build()));
//        assertTrue(exception.getMessage().contains("Grade is invalid."));
//    }
//
//    @Test
//    void should_throw_grade_invalid_exception_when_grade_is_larger_than_nine() {
//        Exception exception = assertThrows(GradeInvalidException.class, () -> validationService.validateStudentData(
//                StudentSubmitRequest.builder()
//                        .name("name")
//                        .classroom(1)
//                        .grade(10)
//                        .build()));
//        assertTrue(exception.getMessage().contains("Grade is invalid."));
//    }
//
//    @Test
//    void should_throw_classroom_invalid_exception_when_classroom_is_smaller_than_one() {
//        Exception exception = assertThrows(ClassroomInvalidException.class, () -> validationService.validateStudentData(
//                StudentSubmitRequest.builder()
//                        .name("name")
//                        .classroom(0)
//                        .grade(1)
//                        .build()));
//        assertTrue(exception.getMessage().contains("Classroom is invalid."));
//    }
//
//    @Test
//    void should_throw_classroom_invalid_exception_when_classroom_is_larger_than_twenty() {
//        Exception exception = assertThrows(ClassroomInvalidException.class, () -> validationService.validateStudentData(
//                StudentSubmitRequest.builder()
//                        .name("name")
//                        .classroom(21)
//                        .grade(1)
//                        .build()));
//        assertTrue(exception.getMessage().contains("Classroom is invalid."));
//    }
//
//    @Test
//    void should_throw_data_not_found_exception_when_id_is_not_in_the_table() {
//        when(studentsService.getTheStudentResponse(2)).thenReturn(Optional.empty());
//        Exception exception = assertThrows(DataNotFoundException.class, () -> validationService.getTheExistedStudentData(2));
//        assertTrue(exception.getMessage().contains("Data is not found."));
//    }
//
//    @Test
//    void should_return_valid_student_data_when_id_is_in_the_table(){
//        when(studentsService.getTheStudentResponse(1))
//                .thenReturn(Optional.ofNullable(StudentEntity.Student.builder()
//                        .name("name")
//                        .id(1)
//                        .classroom(1)
//                        .grade(1)
//                        .build()));
//        Optional<StudentEntity.Student> theExistedStudentData = validationService.getTheExistedStudentData(1);
//        assertThat(theExistedStudentData.isPresent()).isTrue();
//        assertThat(theExistedStudentData.get().getName()).isEqualTo("name");
//    }
//
//
//}