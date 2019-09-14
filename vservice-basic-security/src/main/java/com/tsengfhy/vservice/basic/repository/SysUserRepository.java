package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysUser;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface SysUserRepository extends AgileJpaRepository<SysUser, Long>, QuerydslPredicateExecutor<SysUser> {

    Optional<SysUser> findFirstByUsernameOrMail(String username, String mail);

    Optional<SysUser> findFirstByPhone(String phone);

    Optional<SysUser> findFirstByToken(String token);
}
