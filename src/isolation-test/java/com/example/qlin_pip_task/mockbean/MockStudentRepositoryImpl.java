package com.example.qlin_pip_task.mockbean;

import com.example.qlin_pip_task.dto.StudentResponse;
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
    public List<StudentResponse.Student> findAll() {
        return null;
    }

    @Override
    public List<StudentResponse.Student> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<StudentResponse.Student> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<StudentResponse.Student> findAllById(Iterable<Integer> integers) {
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
    public void delete(StudentResponse.Student entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends StudentResponse.Student> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends StudentResponse.Student> S save(S entity) {
        return null;
    }

    @Override
    public <S extends StudentResponse.Student> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<StudentResponse.Student> findById(Integer integer) {
        return Optional.of(StudentResponse.Student.builder()
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
    public <S extends StudentResponse.Student> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends StudentResponse.Student> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<StudentResponse.Student> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public StudentResponse.Student getOne(Integer integer) {
        return null;
    }

    @Override
    public StudentResponse.Student getById(Integer integer) {
        return null;
    }

    @Override
    public StudentResponse.Student getReferenceById(Integer integer) {
        return null;
    }

    @Override
    public <S extends StudentResponse.Student> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends StudentResponse.Student> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends StudentResponse.Student> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends StudentResponse.Student> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends StudentResponse.Student> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends StudentResponse.Student> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends StudentResponse.Student, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}


