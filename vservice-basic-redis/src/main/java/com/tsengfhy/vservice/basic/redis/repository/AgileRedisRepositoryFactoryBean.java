package com.tsengfhy.vservice.basic.redis.repository;

import com.tsengfhy.vservice.basic.repository.AgileKeyValueRepository;
import com.tsengfhy.vservice.basic.repository.impl.AgileKeyValueRepositoryImpl;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.support.QuerydslKeyValueRepository;
import org.springframework.data.keyvalue.repository.support.SimpleKeyValueRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.data.redis.repository.support.RedisRepositoryFactory;
import org.springframework.data.redis.repository.support.RedisRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;

import java.io.Serializable;

public class AgileRedisRepositoryFactoryBean<R extends Repository<T, ID>, T, ID extends Serializable> extends RedisRepositoryFactoryBean<R, T, ID> {

    public AgileRedisRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RedisRepositoryFactory createRepositoryFactory(KeyValueOperations operations, Class<? extends AbstractQueryCreator<?, ?>> queryCreator, Class<? extends RepositoryQuery> repositoryQueryType) {
        return new RedisRepositoryFactory(operations, queryCreator, repositoryQueryType) {

            @Override
            protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

                Class<?> repositoryInterface = metadata.getRepositoryInterface();

                if (QuerydslUtils.QUERY_DSL_PRESENT && AgileKeyValueRepository.class.isAssignableFrom(repositoryInterface)) {
                    return AgileKeyValueRepositoryImpl.class;
                } else if (QuerydslUtils.QUERY_DSL_PRESENT && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface)) {
                    return QuerydslKeyValueRepository.class;
                } else {
                    return SimpleKeyValueRepository.class;
                }
            }
        };
    }
}
