package com.tsengfhy.vservice.basic.elasticsearch.repository;

import com.tsengfhy.vservice.basic.repository.AgileElasticsearchRepository;
import com.tsengfhy.vservice.basic.repository.impl.AgileElasticsearchRepositoryImpl;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactory;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactoryBean;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

public class AgileElasticsearchRepositoryFactoryBean<R extends Repository<T, ID>, T, ID extends Serializable> extends ElasticsearchRepositoryFactoryBean<R, T, ID> {

    private ElasticsearchOperations operations;

    public AgileElasticsearchRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    public void setElasticsearchOperations(ElasticsearchOperations operations) {
        super.setElasticsearchOperations(operations);
        this.operations = operations;
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory() {
        return new ElasticsearchRepositoryFactory(this.operations) {

            @Override
            protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

                Class<?> repositoryInterface = metadata.getRepositoryInterface();

                if (QuerydslUtils.QUERY_DSL_PRESENT && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface)) {
                    throw new IllegalArgumentException("QueryDsl Support has not been implemented yet.");
                } else if (AgileElasticsearchRepository.class.isAssignableFrom(repositoryInterface)) {
                    return AgileElasticsearchRepositoryImpl.class;
                } else {
                    return super.getRepositoryBaseClass(metadata);
                }
            }
        };
    }
}
