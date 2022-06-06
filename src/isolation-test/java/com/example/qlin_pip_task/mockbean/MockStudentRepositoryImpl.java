package com.example.qlin_pip_task.mockbean;

import com.example.qlin_pip_task.entity.StudentEntity;
import com.example.qlin_pip_task.repository.StudentRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@ActiveProfiles("isolation-test")
@Primary
@Repository
public class MockStudentRepositoryImpl implements StudentRepository {

    @Override
    public List<StudentEntity.Student> findAll() {
        StudentEntity.Student student1 = StudentEntity.Student.builder()
                .name("student1")
                .id(1)
                .classroom(1)
                .grade(1)
                .build();

        StudentEntity.Student student2 = StudentEntity.Student.builder()
                .name("student2")
                .id(2)
                .classroom(2)
                .grade(2)
                .build();

        return List.of(student1, student2);
    }

    @Override
    public List<StudentEntity.Student> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<StudentEntity.Student> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<StudentEntity.Student> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(StudentEntity.Student entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends StudentEntity.Student> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends StudentEntity.Student> S save(S entity) {
        return null;
    }

    @Override
    public <S extends StudentEntity.Student> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<StudentEntity.Student> findById(Integer integer) {
        return Optional.of(StudentEntity.Student.builder()
                .name("student1")
                .id(1)
                .classroom(1)
                .grade(1)
                .build());
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends StudentEntity.Student> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends StudentEntity.Student> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<StudentEntity.Student> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public StudentEntity.Student getOne(Integer integer) {
        return null;
    }

    @Override
    public StudentEntity.Student getById(Integer integer) {
        return null;
    }

    @Override
    public StudentEntity.Student getReferenceById(Integer integer) {
        return null;
    }

    @Override
    public <S extends StudentEntity.Student> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends StudentEntity.Student> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends StudentEntity.Student> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends StudentEntity.Student> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends StudentEntity.Student> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends StudentEntity.Student> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends StudentEntity.Student, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}


