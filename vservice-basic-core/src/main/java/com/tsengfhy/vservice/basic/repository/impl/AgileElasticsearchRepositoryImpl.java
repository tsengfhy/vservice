package com.tsengfhy.vservice.basic.repository.impl;

import com.tsengfhy.vservice.basic.repository.AgileElasticsearchRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.AbstractElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;

import java.io.Serializable;
import java.util.Optional;

public class AgileElasticsearchRepositoryImpl<T, ID extends Serializable> extends AbstractElasticsearchRepository<T, ID> implements AgileElasticsearchRepository<T, ID> {

    private final ElasticsearchOperations elasticsearchOperations;

    public AgileElasticsearchRepositoryImpl(ElasticsearchEntityInformation<T, ID> entityInformation, ElasticsearchOperations elasticsearchOperations) {
        super(entityInformation, elasticsearchOperations);
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    protected String stringIdRepresentation(ID id) {

        return Optional.ofNullable(id).map(val -> {
            if (val instanceof String) {
                return (String) val;
            }

            return val.toString();
        }).orElse(null);
    }
}
