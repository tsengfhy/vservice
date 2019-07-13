package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysSchedulerState;
import com.tsengfhy.vservice.basic.domain.key.SysSchedulerStatePK;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SysSchedulerStateRepository extends AgileJpaRepository<SysSchedulerState, SysSchedulerStatePK> {

    @Query("select t from SysSchedulerState t where t.id.schedName = ?1 and t.id.instanceName = ?2")
    Optional<SysSchedulerState> findById(String schedName, String instanceName);
}
