package com.tsengfhy.vservice.basic.config;

import com.tsengfhy.vservice.basic.jpa.repository.AgileJpaRepositoryFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Slf4j
@Configuration
@ConditionalOnClass({EntityManager.class})
@ConditionalOnBean({DataSource.class})
@AutoConfigureAfter({HibernateJpaAutoConfiguration.class, TaskExecutionAutoConfiguration.class})
@EntityScan(value = "com.tsengfhy")
public class JPAConfig {

    @PostConstruct
    public void init() {
        log.info("VService module [JPA] is loaded");
    }

    @Configuration
    @EnableTransactionManagement
    @DependsOn("transactionManager")
    static class TransactionManagerConfig implements TransactionManagementConfigurer {

        @Resource
        private AbstractPlatformTransactionManager transactionManager;

        @Override
        public PlatformTransactionManager annotationDrivenTransactionManager() {
            this.transactionManager.setGlobalRollbackOnParticipationFailure(false);
            return this.transactionManager;
        }
    }

    @Configuration
    @ConditionalOnClass({JpaRepository.class})
    @ConditionalOnProperty(name = "spring.data.jpa.repositories.enabled", havingValue = "true", matchIfMissing = true)
    @EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
    @EnableJpaAuditing
    @EnableJpaRepositories(
            basePackages = "",
            includeFilters = {
                    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JpaRepository.class)
            },
            repositoryFactoryBeanClass = AgileJpaRepositoryFactoryBean.class
    )
    static class RepositoryConfig {
    }
}
