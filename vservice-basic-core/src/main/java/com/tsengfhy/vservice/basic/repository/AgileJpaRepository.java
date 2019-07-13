package com.tsengfhy.vservice.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@NoRepositoryBean
@Transactional(readOnly = true)
public interface AgileJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    // TODO
}
