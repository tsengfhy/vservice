package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysClientRole;
import com.tsengfhy.vservice.basic.domain.key.SysClientRolePK;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SysClientRoleRepository extends AgileJpaRepository<SysClientRole, SysClientRolePK> {

    @Query("select t from SysClientRole t where t.id.clientId = ?1 and t.id.roleId = ?2")
    Optional<SysClientRole> findById(Long clientId, Long roleId);

    @Query("select t.id.roleId from SysClientRole t where t.id.clientId = ?1")
    List<Long> findAuthorities(Long clientId);
}
