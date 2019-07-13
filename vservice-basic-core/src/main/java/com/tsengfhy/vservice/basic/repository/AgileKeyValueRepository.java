package com.tsengfhy.vservice.basic.repository;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface AgileKeyValueRepository<T, ID extends Serializable> extends KeyValueRepository<T, ID>, QuerydslPredicateExecutor<T> {
    // TODO
}
