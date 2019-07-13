package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysPausedTriggerGrps;
import com.tsengfhy.vservice.basic.domain.key.SysPausedTriggerGrpsPK;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SysPausedTriggerGrpsRepository extends AgileJpaRepository<SysPausedTriggerGrps, SysPausedTriggerGrpsPK> {

    @Query("select t from SysPausedTriggerGrps t where t.id.schedName = ?1 and t.id.triggerGroup = ?2")
    Optional<SysPausedTriggerGrps> findById(String schedName, String triggerGroup);
}
