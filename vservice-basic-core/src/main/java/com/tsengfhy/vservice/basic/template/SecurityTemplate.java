package com.tsengfhy.vservice.basic.template;

import com.tsengfhy.vservice.basic.exception.security.SecurityPersistenceException;
import com.tsengfhy.vservice.basic.dto.security.*;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface SecurityTemplate {

    boolean isExistsResource(ResourceDto dto);

    void saveResource(ResourceDto dto) throws SecurityPersistenceException;

    void updateResource(ResourceDto dto) throws SecurityPersistenceException;

    ResourceDto findResourceById(@NotNull Long id) throws SecurityPersistenceException;

    List<ResourceDto> findResourceByParent(@NotNull Long parent);

    void deleteResource(@NotNull Long id) throws SecurityPersistenceException;

    boolean isExistsRole(RoleDto dto);

    void saveRole(RoleDto dto) throws SecurityPersistenceException;

    void updateRole(RoleDto dto) throws SecurityPersistenceException;

    RoleDto findRoleById(@NotNull Long id) throws SecurityPersistenceException;

    List<RoleDto> findRoleByParent(@NotNull Long parent);

    void deleteRole(@NotNull Long id) throws SecurityPersistenceException;

    boolean isExistsUser(UserDto dto);

    void saveUser(UserDto dto) throws SecurityPersistenceException;

    void updateUser(UserDto dto) throws SecurityPersistenceException;

    UserDto findUserById(@NotNull Long id) throws SecurityPersistenceException;

    Page<UserDto> findUser(UserDto dto, int pageNo, int pageSize, String sort);

    void deleteUser(@NotNull Long id) throws SecurityPersistenceException;

    boolean isExistsGroup(GroupDto dto);

    void saveGroup(GroupDto dto) throws SecurityPersistenceException;

    void updateGroup(GroupDto dto) throws SecurityPersistenceException;

    GroupDto findGroupById(@NotNull Long id) throws SecurityPersistenceException;

    List<GroupDto> findGroupByParent(@NotNull Long parent);

    void deleteGroup(@NotNull Long id) throws SecurityPersistenceException;

    boolean isExistsClient(ClientDto dto);

    void saveClient(ClientDto dto) throws SecurityPersistenceException;

    void updateClient(ClientDto dto) throws SecurityPersistenceException;

    ClientDto findClientById(@NotNull Long id) throws SecurityPersistenceException;

    Page<ClientDto> findClient(ClientDto dto, int pageNo, int pageSize, String sort);

    void deleteClient(@NotNull Long id) throws SecurityPersistenceException;
}
