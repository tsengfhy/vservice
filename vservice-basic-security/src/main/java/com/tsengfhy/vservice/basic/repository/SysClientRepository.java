package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysClient;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysClientRepository extends AgileJpaRepository<SysClient, Long>, QuerydslPredicateExecutor<SysClient> {
}
