package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysMailAccount;

import java.util.Optional;

public interface SysMailAccountRepository extends AgileJpaRepository<SysMailAccount, Long> {

    Optional<SysMailAccount> findFirstByUsername(String username);
}