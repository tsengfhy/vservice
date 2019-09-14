package com.tsengfhy.vservice.basic.template.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.tsengfhy.vservice.basic.domain.*;
import com.tsengfhy.vservice.basic.dto.security.*;
import com.tsengfhy.vservice.basic.exception.security.*;
import com.tsengfhy.vservice.basic.repository.*;
import com.tsengfhy.vservice.basic.template.SecurityTemplate;
import com.tsengfhy.vservice.basic.utils.DataUtils;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Setter
public class JdbcSecurityTemplate implements SecurityTemplate, InitializingBean {

    private SysResourceRepository sysResourceRepository;

    private SysRoleRepository sysRoleRepository;

    private SysUserRepository sysUserRepository;

    private SysGroupRepository sysGroupRepository;

    private SysClientRepository sysClientRepository;

    private PasswordEncoder passwordEncoder;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.sysResourceRepository, "SysResourceRepository must not be null!");
        Assert.notNull(this.sysRoleRepository, "SysRoleRepository must not be null!");
        Assert.notNull(this.sysUserRepository, "SysUserRepository must not be null!");
        Assert.notNull(this.sysGroupRepository, "SysGroupRepository must not be null!");
        Assert.notNull(this.sysClientRepository, "SysClientRepository must not be null!");
        Assert.notNull(this.passwordEncoder, "PasswordEncoder must not be null!");
    }

    @Override
    public boolean isExistsResource(ResourceDto dto) {

        if (!StringUtils.isNotBlank(dto.getUrl()) && !StringUtils.isNotBlank(dto.getMethod())) {
            return false;
        }

        QSysResource qSysResource = QSysResource.sysResource;

        Set<Predicate> set = new HashSet<>();
        Optional.ofNullable(dto.getId()).ifPresent(id -> set.add(qSysResource.id.ne(id)));
        Optional.ofNullable(dto.getUrl()).ifPresent(url -> set.add(qSysResource.url.eq(url)));
        Optional.ofNullable(dto.getMethod()).ifPresent(method -> set.add(qSysResource.method.eq(method.toLowerCase()).or(qSysResource.method.isNull())));

        return sysResourceRepository.exists(ExpressionUtils.allOf(set));
    }

    @Override
    @Transactional
    public void saveResource(ResourceDto dto) throws SecurityPersistenceException {

        if (isExistsResource(dto)) {
            throw new ResourceAlreadyExistsException(MessageSourceUtils.getMessage("Security.persistence.resourceExists", new Object[]{dto}, "Resource " + dto + " has already been created"));
        }

        SysResource sysResource = new SysResource();
        BeanUtils.copyProperties(dto, sysResource);

        Optional.ofNullable(sysResource.getMethod()).map(String::toLowerCase).ifPresent(sysResource::setMethod);

        Long pid = dto.getPid();
        if (pid != null) {
            sysResource.setParent(sysResourceRepository.findById(pid).orElseThrow(() -> new NoSuchResourceException(MessageSourceUtils.getMessage("Security.persistence.noResource", new Object[]{pid}, "No resource with key: " + pid))));

            if (sysResource.getSeries() == null) {
                sysResource.setSeries(sysResourceRepository.generateSeries(pid));
            }
        }

        sysResourceRepository.save(sysResource);
        BeanUtils.copyProperties(sysResource, dto);
    }

    @Override
    @Transactional
    public void updateResource(ResourceDto dto) throws SecurityPersistenceException {

        if (isExistsResource(dto)) {
            throw new ResourceAlreadyExistsException(MessageSourceUtils.getMessage("Security.persistence.resourceExists", new Object[]{dto}, "Resource " + dto + " has already been created"));
        }

        SysResource sysResource = sysResourceRepository.findById(dto.getId()).orElseThrow(() -> new NoSuchResourceException(MessageSourceUtils.getMessage("Security.persistence.noResource", new Object[]{dto.getId() + ""}, "No resource with key: " + dto.getId())));
        BeanUtils.copyProperties(dto, sysResource);

        Optional.ofNullable(sysResource.getMethod()).map(String::toLowerCase).ifPresent(sysResource::setMethod);

        Long pid = dto.getPid();
        if (pid != null && Optional.ofNullable(sysResource.getParent()).map(SysResource::getId).map(parentId -> !pid.equals(parentId)).orElse(true)) {
            sysResource.setParent(sysResourceRepository.findById(pid).orElseThrow(() -> new NoSuchResourceException(MessageSourceUtils.getMessage("Security.persistence.noResource", new Object[]{pid}, "No resource with key: " + pid))));

            if (sysResource.getSeries() == null) {
                sysResource.setSeries(sysResourceRepository.generateSeries(pid));
            }
        }

        sysResourceRepository.save(sysResource);
        BeanUtils.copyProperties(sysResource, dto);
    }

    @Override
    public ResourceDto findResourceById(Long id) throws SecurityPersistenceException {

        SysResource sysResource = sysResourceRepository.findById(id).orElseThrow(() -> new NoSuchResourceException(MessageSourceUtils.getMessage("Security.persistence.noResource", new Object[]{id + ""}, "No resource with key: " + id)));

        ResourceDto dto = new ResourceDto();
        BeanUtils.copyProperties(sysResource, dto);

        Optional.ofNullable(sysResource.getParent()).map(SysResource::getId).ifPresent(dto::setPid);

        return dto;
    }

    @Override
    public List<ResourceDto> findResourceByParent(Long parent) {

        SysResource sysResource = new SysResource();
        SysResource pSysResource = new SysResource();
        pSysResource.setId(parent);
        sysResource.setParent(pSysResource);

        return sysResourceRepository.findAll(Example.of(sysResource))
                .stream()
                .map(resource -> {
                    ResourceDto dto = new ResourceDto();
                    BeanUtils.copyProperties(resource, dto);
                    dto.setPid(parent);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteResource(Long id) throws SecurityPersistenceException {

        try {
            sysResourceRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new NoSuchResourceException(MessageSourceUtils.getMessage("Security.persistence.noResource", new Object[]{id + ""}, "No resource with key: " + id));
        }
    }

    @Override
    public boolean isExistsRole(RoleDto dto) {

        if (!StringUtils.isNotBlank(dto.getName())) {
            return true;
        }

        QSysRole qSysRole = QSysRole.sysRole;

        Set<Predicate> set = new HashSet<>();
        Optional.ofNullable(dto.getId()).ifPresent(id -> set.add(qSysRole.id.ne(id)));
        Optional.ofNullable(dto.getName()).ifPresent(name -> set.add(qSysRole.name.eq(name)));

        return sysRoleRepository.exists(ExpressionUtils.allOf(set));
    }

    @Override
    @Transactional
    public void saveRole(RoleDto dto) throws SecurityPersistenceException {

        if (isExistsRole(dto)) {
            throw new RoleAlreadyExistsException(MessageSourceUtils.getMessage("Security.persistence.roleExists", new Object[]{dto}, "Role " + dto + " has already been created"));
        }

        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(dto, sysRole);

        Long pid = dto.getPid();
        if (pid != null) {
            sysRole.setParent(sysRoleRepository.findById(pid).orElseThrow(() -> new NoSuchRoleException(MessageSourceUtils.getMessage("Security.persistence.noRole", new Object[]{pid}, "No role with key: " + pid))));

            if (sysRole.getSeries() == null) {
                sysRole.setSeries(sysRoleRepository.generateSeries(dto.getPid()));
            }
        }

        sysRole.getSysResources().addAll(
                dto.getResources()
                        .stream()
                        .map(id -> sysResourceRepository.findById(id).orElseThrow(() -> new NoSuchResourceException(MessageSourceUtils.getMessage("Security.persistence.noResource", new Object[]{id}, "No resource with key: " + id))))
                        .collect(Collectors.toSet())
        );

        sysRoleRepository.save(sysRole);
        BeanUtils.copyProperties(sysRole, dto);
    }

    @Override
    @Transactional
    public void updateRole(RoleDto dto) throws SecurityPersistenceException {

        if (isExistsRole(dto)) {
            throw new RoleAlreadyExistsException(MessageSourceUtils.getMessage("Security.persistence.roleExists", new Object[]{dto}, "Role " + dto + " has already been created"));
        }

        SysRole sysRole = sysRoleRepository.findById(dto.getId()).orElseThrow(() -> new NoSuchRoleException(MessageSourceUtils.getMessage("Security.persistence.noRole", new Object[]{dto.getId()}, "No role with key: " + dto.getId())));
        BeanUtils.copyProperties(dto, sysRole);

        Long pid = dto.getPid();
        if (pid != null && Optional.ofNullable(sysRole.getParent()).map(SysRole::getId).map(parentId -> !pid.equals(parentId)).orElse(true)) {
            sysRole.setParent(sysRoleRepository.findById(pid).orElseThrow(() -> new NoSuchRoleException(MessageSourceUtils.getMessage("Security.persistence.noRole", new Object[]{pid}, "No role with key: " + pid))));

            if (sysRole.getSeries() == null) {
                sysRole.setSeries(sysRoleRepository.generateSeries(dto.getPid()));
            }
        }

        Map<Long, SysResource> map = sysRole.getSysResources().stream().collect(Collectors.toConcurrentMap(SysResource::getId, resource -> resource));

        List<Function<Long, SysResource>> list = new ArrayList<>();
        list.add(map::get);
        list.add(id -> sysResourceRepository.findById(id).orElseThrow(() -> new NoSuchResourceException(MessageSourceUtils.getMessage("Security.persistence.noResource", new Object[]{id + ""}, "No resource with key: " + id))));

        sysRole.getSysResources().clear();
        sysRole.getSysResources().addAll(
                dto.getResources()
                        .stream()
                        .map(id -> list.stream().map(function -> function.apply(id)).filter(Objects::nonNull).findFirst().get())
                        .collect(Collectors.toSet())
        );

        sysRoleRepository.save(sysRole);
        BeanUtils.copyProperties(sysRole, dto);
    }

    @Override
    public RoleDto findRoleById(Long id) throws SecurityPersistenceException {

        SysRole sysRole = sysRoleRepository.findById(id).orElseThrow(() -> new NoSuchRoleException(MessageSourceUtils.getMessage("Security.persistence.noRole", new Object[]{id}, "No role with key: " + id)));

        RoleDto dto = new RoleDto();
        BeanUtils.copyProperties(sysRole, dto);

        Optional.ofNullable(sysRole.getParent()).map(SysRole::getId).ifPresent(dto::setPid);

        dto.getResources().addAll(sysRole.getSysResources().stream().map(SysResource::getId).collect(Collectors.toSet()));

        return dto;
    }

    @Override
    public List<RoleDto> findRoleByParent(Long parent) {

        SysRole sysRole = new SysRole();
        SysRole pSysRole = new SysRole();
        pSysRole.setId(parent);
        sysRole.setParent(pSysRole);

        return sysRoleRepository.findAll(Example.of(sysRole))
                .stream()
                .map(role -> {
                    RoleDto dto = new RoleDto();
                    BeanUtils.copyProperties(role, dto);
                    dto.setPid(parent);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRole(Long id) throws SecurityPersistenceException {

        try {
            sysRoleRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new NoSuchRoleException(MessageSourceUtils.getMessage("Security.persistence.noRole", new Object[]{id + ""}, "No role exists with id: " + id));
        }
    }

    @Override
    public boolean isExistsUser(UserDto dto) {

        if (!StringUtils.isNotBlank(dto.getUsername()) && !StringUtils.isNotBlank(dto.getMail()) && !StringUtils.isNotBlank(dto.getPhone()) && !StringUtils.isNotBlank(dto.getToken())) {
            return true;
        }

        QSysUser qSysUser = QSysUser.sysUser;

        Set<Predicate> set = new HashSet<>();
        Optional.ofNullable(dto.getUsername()).ifPresent(username -> set.add(qSysUser.username.eq(username)));
        Optional.ofNullable(dto.getMail()).ifPresent(mail -> set.add(qSysUser.mail.eq(mail)));
        Optional.ofNullable(dto.getPhone()).ifPresent(phone -> set.add(qSysUser.phone.eq(phone)));
        Optional.ofNullable(dto.getToken()).ifPresent(token -> set.add(qSysUser.token.eq(token)));

        Predicate predicate = Optional.ofNullable(dto.getId()).map(id -> ExpressionUtils.and(qSysUser.id.ne(id), (ExpressionUtils.anyOf(set)))).orElseGet(() -> ExpressionUtils.anyOf(set));

        return sysUserRepository.exists(predicate);
    }

    @Override
    @Transactional
    public void saveUser(UserDto dto) throws SecurityPersistenceException {

        if (isExistsUser(dto)) {
            throw new UserAlreadyExistsException(MessageSourceUtils.getMessage("Security.persistence.userExists", new Object[]{dto}, "User " + dto + " has already been created"));
        }

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(dto, sysUser);

        Optional.ofNullable(dto.getPassword()).filter(StringUtils::isNotBlank).map(passwordEncoder::encode).ifPresent(sysUser::setPassword);
        sysUser.setEnabled("1");

        Long gid = dto.getGroupId();
        if (gid != null) {
            sysUser.setSysGroup(sysGroupRepository.findById(gid).orElseThrow(() -> new NoSuchGroupException(MessageSourceUtils.getMessage("Security.persistence.noGroup", new Object[]{gid}, "No group with key: " + gid))));
        }

        sysUser.getSysRoles().addAll(
                dto.getRoles()
                        .stream()
                        .map(id -> sysRoleRepository.findById(id).orElseThrow(() -> new NoSuchRoleException(MessageSourceUtils.getMessage("Security.persistence.noRole", new Object[]{id}, "No role with key: " + id))))
                        .collect(Collectors.toSet())
        );

        sysUserRepository.save(sysUser);
        BeanUtils.copyProperties(sysUser, dto);
    }

    @Override
    @Transactional
    public void updateUser(UserDto dto) throws SecurityPersistenceException {

        if (isExistsUser(dto)) {
            throw new UserAlreadyExistsException(MessageSourceUtils.getMessage("Security.persistence.userExists", new Object[]{dto}, "User " + dto + " has already been created"));
        }

        SysUser sysUser = sysUserRepository.findById(dto.getId()).orElseThrow(() -> new NoSuchUserException(MessageSourceUtils.getMessage("Security.persistence.noUser", new Object[]{dto.getId()}, "No user with key: " + dto.getId())));
        BeanUtils.copyProperties(dto, sysUser);

        Optional.ofNullable(dto.getPassword()).filter(StringUtils::isNotBlank).filter(rawPassword -> !passwordEncoder.matches(rawPassword, sysUser.getPassword())).map(passwordEncoder::encode).ifPresent(sysUser::setPassword);

        Long gid = dto.getGroupId();
        if (gid != null && Optional.ofNullable(sysUser.getSysGroup()).map(SysGroup::getId).map(groupId -> !gid.equals(groupId)).orElse(true)) {
            sysUser.setSysGroup(sysGroupRepository.findById(gid).orElseThrow(() -> new NoSuchGroupException(MessageSourceUtils.getMessage("Security.persistence.noGroup", new Object[]{gid}, "No group with key: " + gid))));
        }

        Map<Long, SysRole> map = sysUser.getSysRoles().stream().collect(Collectors.toConcurrentMap(SysRole::getId, group -> group));

        List<Function<Long, SysRole>> list = new ArrayList<>();
        list.add(map::get);
        list.add(id -> sysRoleRepository.findById(id).orElseThrow(() -> new NoSuchRoleException(MessageSourceUtils.getMessage("Security.persistence.noRole", new Object[]{id}, "No role with key: " + id))));

        sysUser.getSysRoles().clear();
        sysUser.getSysRoles().addAll(
                dto.getRoles()
                        .stream()
                        .map(id -> list.stream().map(function -> function.apply(id)).filter(Objects::nonNull).findFirst().get())
                        .collect(Collectors.toSet())
        );

        sysUserRepository.save(sysUser);
        BeanUtils.copyProperties(sysUser, dto);
    }

    @Override
    public UserDto findUserById(Long id) throws SecurityPersistenceException {

        SysUser sysUser = sysUserRepository.findById(id).orElseThrow(() -> new NoSuchUserException(MessageSourceUtils.getMessage("Security.persistence.noUser", new Object[]{id}, "No user with key: " + id)));

        UserDto dto = new UserDto();
        BeanUtils.copyProperties(sysUser, dto);

        Optional.ofNullable(sysUser.getSysGroup()).map(SysGroup::getId).ifPresent(dto::setGroupId);

        dto.getRoles().addAll(sysUser.getSysRoles().stream().map(SysRole::getId).collect(Collectors.toSet()));

        return dto;
    }

    @Override
    public Page<UserDto> findUser(UserDto dto, int pageNo, int pageSize, String sort) {

        SysUser sysUser = new SysUser();
        sysUser.setUsername(dto.getUsername());
        sysUser.setMail(dto.getMail());
        sysUser.setPhone(dto.getPhone());
        sysUser.setToken(dto.getToken());
        sysUser.setLocked(dto.getLocked());
        sysUser.setEnabled(dto.getEnabled());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withMatcher("token", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("locked", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("enabled", ExampleMatcher.GenericPropertyMatchers.exact());

        Page<SysUser> page = sysUserRepository.findAll(Example.of(sysUser, matcher), PageRequest.of(pageNo - 1, pageSize, DataUtils.toSort(sort)));
        List<UserDto> list = page.getContent()
                .stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    BeanUtils.copyProperties(user, userDto);

                    Optional.ofNullable(user.getSysGroup()).map(SysGroup::getId).ifPresent(userDto::setGroupId);

                    userDto.getRoles().addAll(user.getSysRoles().stream().map(SysRole::getId).collect(Collectors.toSet()));

                    return userDto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }

    @Override
    @Transactional
    public void deleteUser(Long id) throws SecurityPersistenceException {

        try {
            sysUserRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new NoSuchUserException(MessageSourceUtils.getMessage("Security.persistence.noUser", new Object[]{id + ""}, "No user with key: " + id));
        }
    }

    @Override
    public boolean isExistsGroup(GroupDto dto) {

        if (!StringUtils.isNotBlank(dto.getName())) {
            return true;
        }

        QSysGroup qSysGroup = QSysGroup.sysGroup;

        Set<Predicate> set = new HashSet<>();
        Optional.ofNullable(dto.getId()).ifPresent(id -> set.add(qSysGroup.id.ne(id)));
        Optional.ofNullable(dto.getName()).ifPresent(name -> set.add(qSysGroup.name.eq(name)));

        return sysGroupRepository.exists(ExpressionUtils.allOf(set));
    }

    @Override
    @Transactional
    public void saveGroup(GroupDto dto) throws SecurityPersistenceException {

        if (isExistsGroup(dto)) {
            throw new GroupAlreadyExistsException(MessageSourceUtils.getMessage("Security.persistence.groupExists", new Object[]{dto}, "Group " + dto + " has already been created"));
        }

        SysGroup sysGroup = new SysGroup();
        BeanUtils.copyProperties(dto, sysGroup);

        Long pid = dto.getPid();
        if (pid != null) {
            sysGroup.setParent(sysGroupRepository.findById(pid).orElseThrow(() -> new NoSuchGroupException(MessageSourceUtils.getMessage("Security.persistence.noGroup", new Object[]{pid}, "No group with key: " + pid))));

            if (sysGroup.getSeries() == null) {
                sysGroup.setSeries(sysGroupRepository.generateSeries(pid));
            }
        }

        sysGroupRepository.save(sysGroup);
        BeanUtils.copyProperties(sysGroup, dto);
    }

    @Override
    @Transactional
    public void updateGroup(GroupDto dto) throws SecurityPersistenceException {

        if (isExistsGroup(dto)) {
            throw new GroupAlreadyExistsException(MessageSourceUtils.getMessage("Security.persistence.groupExists", new Object[]{dto}, "Group " + dto + " has already been created"));
        }

        SysGroup sysGroup = sysGroupRepository.findById(dto.getId()).orElseThrow(() -> new NoSuchGroupException(MessageSourceUtils.getMessage("Security.persistence.noGroup", new Object[]{dto.getId()}, "No group with key: " + dto.getId())));
        BeanUtils.copyProperties(dto, sysGroup);

        Long pid = dto.getPid();
        if (pid != null && Optional.ofNullable(sysGroup.getParent()).map(SysGroup::getId).map(parentId -> !pid.equals(parentId)).orElse(true)) {
            sysGroup.setParent(sysGroupRepository.findById(pid).orElseThrow(() -> new NoSuchGroupException(MessageSourceUtils.getMessage("Security.persistence.noGroup", new Object[]{pid}, "No group with key: " + pid))));

            if (sysGroup.getSeries() == null) {
                sysGroup.setSeries(sysGroupRepository.generateSeries(dto.getPid()));
            }
        }

        sysGroupRepository.save(sysGroup);
        BeanUtils.copyProperties(sysGroup, dto);
    }

    @Override
    public GroupDto findGroupById(Long id) throws SecurityPersistenceException {

        SysGroup sysGroup = sysGroupRepository.findById(id).orElseThrow(() -> new NoSuchGroupException(MessageSourceUtils.getMessage("Security.persistence.noGroup", new Object[]{id}, "No group with key: " + id)));

        GroupDto dto = new GroupDto();
        BeanUtils.copyProperties(sysGroup, dto);

        Optional.ofNullable(sysGroup.getParent()).map(SysGroup::getId).ifPresent(dto::setPid);

        dto.getUsers().addAll(sysGroup.getSysUsers().stream().map(SysUser::getId).collect(Collectors.toSet()));

        return dto;
    }

    @Override
    public List<GroupDto> findGroupByParent(Long parent) {

        SysGroup sysGroup = new SysGroup();
        SysGroup pSysGroup = new SysGroup();
        pSysGroup.setId(parent);
        sysGroup.setParent(pSysGroup);

        return sysGroupRepository.findAll(Example.of(sysGroup))
                .stream()
                .map(group -> {
                    GroupDto dto = new GroupDto();
                    BeanUtils.copyProperties(group, dto);
                    dto.setPid(parent);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteGroup(Long id) throws SecurityPersistenceException {

        try {
            sysGroupRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new NoSuchGroupException(MessageSourceUtils.getMessage("Security.persistence.noGroup", new Object[]{id + ""}, "No group with key: " + id));
        }
    }

    @Override
    public boolean isExistsClient(ClientDto dto) {

        if (!StringUtils.isNotBlank(dto.getName())) {
            return true;
        }

        QSysClient qSysClient = QSysClient.sysClient;

        Set<Predicate> set = new HashSet<>();
        Optional.ofNullable(dto.getId()).ifPresent(id -> set.add(qSysClient.id.ne(id)));
        Optional.ofNullable(dto.getName()).ifPresent(name -> set.add(qSysClient.name.eq(name)));

        return sysClientRepository.exists(ExpressionUtils.allOf(set));
    }

    @Override
    @Transactional
    public void saveClient(ClientDto dto) throws SecurityPersistenceException {

        if (isExistsClient(dto)) {
            throw new ClientAlreadyExistsException(MessageSourceUtils.getMessage("Security.persistence.clientExists", new Object[]{dto}, "Client " + dto + " has already been created"));
        }

        SysClient sysClient = new SysClient();
        BeanUtils.copyProperties(dto, sysClient);

        Optional.ofNullable(dto.getSecret()).filter(StringUtils::isNotBlank).map(passwordEncoder::encode).ifPresent(sysClient::setSecret);
        sysClient.setEnabled("1");

        sysClient.getSysRoles().addAll(
                dto.getRoles()
                        .stream()
                        .map(id -> sysRoleRepository.findById(id).orElseThrow(() -> new NoSuchRoleException(MessageSourceUtils.getMessage("Security.persistence.noRole", new Object[]{id}, "No role with key: " + id))))
                        .collect(Collectors.toSet())
        );

        sysClientRepository.save(sysClient);
        BeanUtils.copyProperties(sysClient, dto);
    }

    @Override
    @Transactional
    public void updateClient(ClientDto dto) throws SecurityPersistenceException {

        if (isExistsClient(dto)) {
            throw new ClientAlreadyExistsException(MessageSourceUtils.getMessage("Security.persistence.clientExists", new Object[]{dto}, "Client " + dto + " has already been created"));
        }

        SysClient sysClient = sysClientRepository.findById(dto.getId()).orElseThrow(() -> new NoSuchClientException(MessageSourceUtils.getMessage("Security.persistence.noClient", new Object[]{dto.getId()}, "No client with key: " + dto.getId())));
        BeanUtils.copyProperties(dto, sysClient);

        Optional.ofNullable(dto.getSecret()).filter(StringUtils::isNotBlank).filter(rawSecret -> !passwordEncoder.matches(rawSecret, sysClient.getSecret())).map(passwordEncoder::encode).ifPresent(sysClient::setSecret);

        Map<Long, SysRole> map = sysClient.getSysRoles().stream().collect(Collectors.toConcurrentMap(SysRole::getId, role -> role));

        List<Function<Long, SysRole>> list = new ArrayList<>();
        list.add(map::get);
        list.add(id -> sysRoleRepository.findById(id).orElseThrow(() -> new NoSuchRoleException(MessageSourceUtils.getMessage("Security.persistence.noRole", new Object[]{id}, "No role with key: " + id))));

        sysClient.getSysRoles().clear();
        sysClient.getSysRoles().addAll(
                dto.getRoles()
                        .stream()
                        .map(id -> list.stream().map(function -> function.apply(id)).filter(Objects::nonNull).findFirst().get())
                        .collect(Collectors.toSet())
        );

        sysClientRepository.save(sysClient);
        BeanUtils.copyProperties(sysClient, dto);
    }

    @Override
    public ClientDto findClientById(Long id) throws SecurityPersistenceException {

        SysClient sysClient = sysClientRepository.findById(id).orElseThrow(() -> new NoSuchClientException(MessageSourceUtils.getMessage("Security.persistence.noClient", new Object[]{id}, "No client with key: " + id)));

        ClientDto dto = new ClientDto();
        BeanUtils.copyProperties(sysClient, dto);

        dto.getRoles().addAll(sysClient.getSysRoles().stream().map(SysRole::getId).collect(Collectors.toSet()));

        return dto;
    }

    @Override
    public Page<ClientDto> findClient(ClientDto dto, int pageNo, int pageSize, String sort) {

        SysClient sysClient = new SysClient();
        sysClient.setName(dto.getName());
        sysClient.setLocked(dto.getLocked());
        sysClient.setEnabled(dto.getEnabled());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withMatcher("locked", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("enabled", ExampleMatcher.GenericPropertyMatchers.exact());

        Page<SysClient> page = sysClientRepository.findAll(Example.of(sysClient, matcher), PageRequest.of(pageNo - 1, pageSize, DataUtils.toSort(sort)));
        List<ClientDto> list = page.getContent()
                .stream()
                .map(client -> {
                    ClientDto clientDto = new ClientDto();
                    BeanUtils.copyProperties(client, clientDto);

                    clientDto.getRoles().addAll(client.getSysRoles().stream().map(SysRole::getId).collect(Collectors.toList()));

                    return clientDto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }

    @Override
    @Transactional
    public void deleteClient(Long id) throws SecurityPersistenceException {

        try {
            sysClientRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new NoSuchClientException(MessageSourceUtils.getMessage("Security.persistence.noClient", new Object[]{id + ""}, "No client with key: " + id));
        }
    }
}
