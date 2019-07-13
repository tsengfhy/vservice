package com.tsengfhy.vservice.basic.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface AgileElasticsearchRepository<T, ID extends Serializable> extends ElasticsearchRepository<T, ID> {
    // TODO
}
