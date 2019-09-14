package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysRoleResource;
import com.tsengfhy.vservice.basic.domain.key.SysRoleResourcePK;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SysRoleResourceRepository extends AgileJpaRepository<SysRoleResource, SysRoleResourcePK> {

    @Query("select t from SysRoleResource t where t.id.roleId = ?1 and t.id.resourceId = ?2")
    Optional<SysRoleResource> findById(Long roleId, Long resourceId);
}
