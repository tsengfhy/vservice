package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysFiredTriggers;
import com.tsengfhy.vservice.basic.domain.key.SysFiredTriggersPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface SysFiredTriggersRepository extends AgileJpaRepository<SysFiredTriggers, SysFiredTriggersPK>, QuerydslPredicateExecutor<SysFiredTriggers> {

    @Query("select t from SysFiredTriggers t where t.id.schedName = ?1 and t.id.entryId = ?2")
    Optional<SysFiredTriggers> findById(String schedName, String entryId);
}
