package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysUserRole;
import com.tsengfhy.vservice.basic.domain.key.SysUserRolePK;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SysUserRoleRepository extends AgileJpaRepository<SysUserRole, SysUserRolePK> {

    @Query("select t from SysUserRole t where t.id.userId = ?1 and t.id.roleId = ?2")
    Optional<SysUserRole> findById(Long userId, Long roleId);

    @Query("select t.id.roleId from SysUserRole t where t.id.userId = ?1")
    List<Long> findAuthorities(Long userId);
}
