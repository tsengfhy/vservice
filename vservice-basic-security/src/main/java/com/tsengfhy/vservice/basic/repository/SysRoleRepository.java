package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysRoleRepository extends AgileJpaRepository<SysRole, Long>, QuerydslPredicateExecutor<SysRole> {

    @Query("select case when max(t.series) is null then 1 else (max(t.series) + 1) end from SysRole t where t.parent.id = ?1")
    int generateSeries(Long pid);
}
