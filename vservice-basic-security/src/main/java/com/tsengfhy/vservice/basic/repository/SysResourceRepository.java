package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface SysResourceRepository extends AgileJpaRepository<SysResource, Long>, QuerydslPredicateExecutor<SysResource> {

    @Query("select t.url, t.method, a.id.roleId from SysResource t left join SysRoleResource a on t.id = a.id.resourceId where t.url is not null")
    List<Object[]> findResourceMapping();

    @Query("select case when max(t.series) is null then 1 else (max(t.series) + 1) end from SysResource t where t.parent.id = ?1")
    int generateSeries(Long pid);
}
