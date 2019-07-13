package com.tsengfhy.vservice.basic.config;

import com.tsengfhy.vservice.basic.elasticsearch.repository.AgileElasticsearchRepositoryFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Client;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@ConditionalOnClass({Client.class})
@ConditionalOnBean({Client.class})
@AutoConfigureAfter({ElasticsearchAutoConfiguration.class})
public class ElasticsearchConfig {

    static {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    @PostConstruct
    public void init() {
        log.info("VService module [Elasticsearch] is loaded");
    }

    @Configuration
    @ConditionalOnClass({ElasticsearchRepository.class})
    @ConditionalOnProperty(name = "spring.data.elasticsearch.repositories.enabled", havingValue = "true", matchIfMissing = true)
    @EnableAutoConfiguration(exclude = ElasticsearchRepositoriesAutoConfiguration.class)
    @EnableElasticsearchRepositories(
            basePackages = "",
            includeFilters = {
                    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ElasticsearchRepository.class)
            },
            repositoryFactoryBeanClass = AgileElasticsearchRepositoryFactoryBean.class
    )
    static class RepositoryConfig {
    }
}
