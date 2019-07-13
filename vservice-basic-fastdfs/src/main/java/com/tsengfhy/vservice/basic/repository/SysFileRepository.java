package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysFileRepository extends AgileJpaRepository<SysFile, Long>, QuerydslPredicateExecutor<SysFile> {

    @Query("select case when max(t.series) is null then 1 else (max(t.series) + 1) end from SysFile t where t.foreignId = ?1 and t.foreignType = ?2")
    int generateSeries(Long foreignId, String foreignType);
}
