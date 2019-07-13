package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysJobDetails;
import com.tsengfhy.vservice.basic.domain.key.SysJobDetailsPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface SysJobDetailsRepository extends AgileJpaRepository<SysJobDetails, SysJobDetailsPK>, QuerydslPredicateExecutor<SysJobDetails> {

    @Query("select t from SysJobDetails t where t.id.schedName = ?1 and t.id.jobName = ?2 and t.id.jobGroup = ?3")
    Optional<SysJobDetails> findById(String schedName, String jobName, String jobGroup);
}
