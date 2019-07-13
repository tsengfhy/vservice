package com.tsengfhy.vservice.basic.jpa.repository;

import com.tsengfhy.vservice.basic.repository.AgileJpaRepository;
import com.tsengfhy.vservice.basic.repository.impl.AgileJpaRepositoryImpl;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class AgileJpaRepositoryFactoryBean<R extends Repository<T, ID>, T, ID extends Serializable> extends JpaRepositoryFactoryBean<R, T, ID> {

    public AgileJpaRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(final EntityManager entityManager) {
        return new JpaRepositoryFactory(entityManager) {

            @Override
            protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

                Class<?> repositoryInterface = metadata.getRepositoryInterface();

                if (AgileJpaRepository.class.isAssignableFrom(repositoryInterface)) {
                    return AgileJpaRepositoryImpl.class;
                } else {
                    return SimpleJpaRepository.class;
                }
            }
        };
    }

}
