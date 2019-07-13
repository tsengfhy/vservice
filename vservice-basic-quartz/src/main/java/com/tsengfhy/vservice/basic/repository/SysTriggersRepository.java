package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysTriggers;
import com.tsengfhy.vservice.basic.domain.key.SysTriggersPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface SysTriggersRepository extends AgileJpaRepository<SysTriggers, SysTriggersPK>, QuerydslPredicateExecutor<SysTriggers> {

    @Query("select t from SysTriggers t where t.id.schedName = ?1 and t.id.triggerName = ?2 and t.id.triggerGroup = ?3")
    Optional<SysTriggers> findById(String schedName, String triggerName, String triggerGroup);
}
