package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysLocks;
import com.tsengfhy.vservice.basic.domain.key.SysLocksPK;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SysLocksRepository extends AgileJpaRepository<SysLocks, SysLocksPK> {

    @Query("select t from SysLocks t where t.id.schedName = ?1 and t.id.lockName = ?2")
    Optional<SysLocks> findById(String schedName, String lockName);
}
