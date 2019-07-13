package com.tsengfhy.vservice.basic.repository.impl;

import com.tsengfhy.vservice.basic.repository.AgileKeyValueRepository;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.support.QuerydslKeyValueRepository;
import org.springframework.data.repository.core.EntityInformation;

import java.io.Serializable;

public class AgileKeyValueRepositoryImpl<T, ID extends Serializable> extends QuerydslKeyValueRepository<T, ID> implements AgileKeyValueRepository<T, ID> {

    private final KeyValueOperations keyValueOperations;

    public AgileKeyValueRepositoryImpl(EntityInformation<T, ID> entityInformation, KeyValueOperations operations) {
        super(entityInformation, operations);
        this.keyValueOperations = operations;
    }
}
