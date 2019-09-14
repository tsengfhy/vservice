package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysGroupRepository extends AgileJpaRepository<SysGroup, Long>, QuerydslPredicateExecutor<SysGroup> {

    @Query("select case when max(t.series) is null then 1 else (max(t.series) + 1) end from SysGroup t where t.parent.id = ?1")
    int generateSeries(Long pid);
}
