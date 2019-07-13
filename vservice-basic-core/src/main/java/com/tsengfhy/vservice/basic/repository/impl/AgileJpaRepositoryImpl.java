package com.tsengfhy.vservice.basic.repository.impl;

import com.tsengfhy.vservice.basic.repository.AgileJpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class AgileJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements AgileJpaRepository<T, ID> {

    private final EntityManager entityManager;

    public AgileJpaRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }
}
