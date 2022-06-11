package com.example.qlin_pip_task.service;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import org.hibernate.dialect.PostgreSQL94Dialect;
import org.springframework.stereotype.Component;

@Component
public class PostgreSQL94CustomDialect extends PostgreSQL94Dialect {

    public PostgreSQL94CustomDialect() {
        super();
        this.registerHibernateType(2003, IntArrayType.class.getName());
    }
}
