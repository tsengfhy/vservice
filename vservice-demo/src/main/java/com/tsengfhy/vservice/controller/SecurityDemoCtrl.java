package com.tsengfhy.vservice.controller;

import com.tsengfhy.vservice.basic.dto.security.*;
import com.tsengfhy.vservice.basic.id.SnowflakeId;
import com.tsengfhy.vservice.basic.template.SecurityTemplate;
import com.tsengfhy.vservice.basic.utils.RestUtils;
import com.tsengfhy.vservice.basic.web.core.Rest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/public/security")
@Api(tags = {"Security Demo"}, description = "A demo of common api for security")
public class SecurityDemoCtrl {

    @Autowired(required = false)
    private SecurityTemplate securityTemplate;

    private Long resourceId;

    private Long resourceIndexId;

    private Long roleId;

    private Long userId;

    private Long groupId;

    private Long clientId;

    @PostMapping(value = "/resource")
    @ApiOperation(value = "Save resource", notes = "Insert resource info into database")
    public Rest<List<ResourceDto>> resourceAdd() {

        ResourceDto dto = new ResourceDto();
        resourceId = SnowflakeId.generate();
        dto.setId(resourceId);
        dto.setName("根");
        dto.setType("1");
        securityTemplate.saveResource(dto);

        ResourceDto dto2 = new ResourceDto();
        resourceIndexId = SnowflakeId.generate();
        dto2.setId(resourceIndexId);
        dto2.setUrl("/index.html");
        dto2.setName("首页");
        dto2.setType("2");
        dto2.setPid(dto.getId());
        securityTemplate.saveResource(dto2);

        ResourceDto dto3 = new ResourceDto();
        dto3.setUrl("/test.html");
        dto3.setName("测试");
        dto3.setType("2");
        dto3.setPid(dto.getId());
        securityTemplate.saveResource(dto3);

        List<ResourceDto> list = new ArrayList<>();
        list.add(dto);
        list.add(dto2);
        list.add(dto3);

        return RestUtils.save(true, "", list);
    }

    @PutMapping(value = "/resource/{id}")
    @ApiOperation(value = "Update resource", notes = "Update resource info by Id")
    public Rest<ResourceDto> resourceUpdate(
            @ApiParam(value = "Resource Id") @PathVariable Long id
    ) {

        ResourceDto dto = securityTemplate.findResourceById(Optional.ofNullable(id).orElse(resourceIndexId));
        dto.setMethod("GET");
        dto.setSeries(3);
        securityTemplate.updateResource(dto);

        return RestUtils.operate(HttpStatus.OK.value(), "", dto);
    }

    @GetMapping(value = "/resource/{pid}")
    @ApiOperation(value = "Get resources", notes = "Get resource info by parentId")
    public Rest<List<ResourceDto>> resourceList(
            @ApiParam(value = "Resource Pid") @PathVariable Long pid
    ) {

        return RestUtils.select(securityTemplate.findResourceByParent(Optional.ofNullable(pid).orElse(resourceIndexId)));
    }

    @DeleteMapping(value = "/resource/{id}")
    @ApiOperation(value = "Delete resource", notes = "Delete resource info by Id")
    public Rest resourceDelete(
            @ApiParam(value = "Resource Id") @PathVariable Long id
    ) {

        securityTemplate.deleteResource(Optional.ofNullable(id).orElse(resourceIndexId));
        return RestUtils.delete(true, "");
    }

    @PostMapping(value = "/role")
    @ApiOperation(value = "Save role", notes = "Insert role info into database")
    public Rest<List<RoleDto>> roleAdd() {

        RoleDto dto = new RoleDto();
        roleId = SnowflakeId.generate();
        dto.setId(roleId);
        dto.setName("测试角色");

        Set<Long> resources = new HashSet<>();
        resources.add(resourceId);
        dto.setResources(resources);

        securityTemplate.saveRole(dto);

        List<RoleDto> list = new ArrayList<>();
        list.add(dto);

        return RestUtils.save(true, "", list);
    }

    @PutMapping(value = "/role/{id}")
    @ApiOperation(value = "Update role", notes = "Update role info by Id")
    public Rest<RoleDto> roleUpdate(
            @ApiParam(value = "Role Id") @PathVariable Long id
    ) {

        RoleDto dto = securityTemplate.findRoleById(Optional.ofNullable(id).orElse(roleId));

        Set<Long> resources = new HashSet<>();
        resources.add(resourceId);
        resources.add(resourceIndexId);
        dto.setResources(resources);

        securityTemplate.updateRole(dto);

        return RestUtils.operate(HttpStatus.OK.value(), "", dto);
    }

    @GetMapping(value = "/role/{pid}")
    @ApiOperation(value = "Get roles", notes = "Get role info by parentId")
    public Rest<List<RoleDto>> roleList(
            @ApiParam(value = "Role Pid") @PathVariable Long pid
    ) {

        return RestUtils.select(securityTemplate.findRoleByParent(Optional.ofNullable(pid).orElse(roleId)));
    }

    @DeleteMapping(value = "/role/{id}")
    @ApiOperation(value = "Delete role", notes = "Delete role info by Id")
    public Rest roleDelete(
            @ApiParam(value = "Role Id") @PathVariable Long id
    ) {

        securityTemplate.deleteRole(Optional.ofNullable(id).orElse(roleId));
        return RestUtils.delete(true, "");
    }

    @PostMapping(value = "/user")
    @ApiOperation(value = "Save rser", notes = "Insert user info into database")
    public Rest<List<UserDto>> userAdd() {

        UserDto dto = new UserDto();
        userId = SnowflakeId.generate();
        dto.setId(userId);
        dto.setUsername("user");
        dto.setPhone("18640883340");
        dto.setMail("296446733@qq.com");
        dto.setToken("123456");
        dto.setPassword("1");

        Set<Long> roles = new HashSet<>();
        roles.add(roleId);
        dto.setRoles(roles);

        securityTemplate.saveUser(dto);

        List<UserDto> list = new ArrayList();
        list.add(dto);
        return RestUtils.save(true, "", list);
    }

    @PutMapping(value = "/user/{id}")
    @ApiOperation(value = "Update rser", notes = "Update user info by Id")
    public Rest<UserDto> userUpdate(
            @ApiParam(value = "User Id") @PathVariable Long id
    ) {

        UserDto dto = securityTemplate.findUserById(Optional.ofNullable(id).orElse(userId));
        dto.setGroupId(groupId);
        dto.setPassword("1");
        securityTemplate.updateUser(dto);

        return RestUtils.operate(HttpStatus.OK.value(), "", dto);
    }

    @GetMapping(value = "/user")
    @ApiOperation(value = "Get users", notes = "Get user info")
    public Rest<Page<UserDto>> userList() {

        UserDto dto = new UserDto();
        dto.setEnabled("1");

        return RestUtils.select(securityTemplate.findUser(dto, 1, 10, "username-"));
    }

    @DeleteMapping(value = "/user/{id}")
    @ApiOperation(value = "Delete rser", notes = "Delete user info by Id")
    public Rest userDelete(
            @ApiParam(value = "User Id") @PathVariable Long id
    ) {

        securityTemplate.deleteUser(Optional.ofNullable(id).orElse(roleId));
        return RestUtils.delete(true, "");
    }

    @PostMapping(value = "/group")
    @ApiOperation(value = "Save group", notes = "Insert group info into database")
    public Rest<List<GroupDto>> groupAdd() {

        GroupDto dto = new GroupDto();
        groupId = SnowflakeId.generate();
        dto.setId(groupId);
        dto.setName("测试机构");
        securityTemplate.saveGroup(dto);

        GroupDto dto2 = new GroupDto();
        dto2.setPid(groupId);
        dto2.setName("测试机构2");
        securityTemplate.saveGroup(dto2);

        List<GroupDto> list = new ArrayList<>();
        list.add(dto);
        list.add(dto2);

        return RestUtils.save(true, "", list);
    }

    @PutMapping(value = "/group/{id}")
    @ApiOperation(value = "Update group", notes = "Update group info by Id")
    public Rest<GroupDto> groupUpdate(
            @ApiParam(value = "Group Id") @PathVariable Long id
    ) {

        GroupDto dto = securityTemplate.findGroupById(Optional.ofNullable(id).orElse(groupId));
        dto.setSeries(3);

        securityTemplate.updateGroup(dto);

        return RestUtils.operate(HttpStatus.OK.value(), "", dto);
    }

    @GetMapping(value = "/group/{pid}")
    @ApiOperation(value = "Get groups", notes = "Get group info by parentId")
    public Rest<List<GroupDto>> groupList(
            @ApiParam(value = "Group Pid") @PathVariable Long pid
    ) {

        return RestUtils.select(securityTemplate.findGroupByParent(Optional.ofNullable(pid).orElse(groupId)));
    }

    @DeleteMapping(value = "/group/{id}")
    @ApiOperation(value = "Delete group", notes = "Delete group info by Id")
    public Rest groupDelete(
            @ApiParam(value = "Group Id") @PathVariable Long id
    ) {

        securityTemplate.deleteGroup(Optional.ofNullable(id).orElse(groupId));
        return RestUtils.delete(true, "");
    }

    @PostMapping(value = "/client")
    @ApiOperation(value = "Save client", notes = "Insert client info into database")
    public Rest<ClientDto> clientAdd() {

        ClientDto dto = new ClientDto();
        clientId = SnowflakeId.generate();
        dto.setId(clientId);
        dto.setName("client");
        dto.setScopes("scope");
        dto.setSecret("123456");
        dto.setGrantTypes("password,refresh_token");

        securityTemplate.saveClient(dto);

        return RestUtils.save(true, "", dto);
    }

    @PutMapping(value = "/client/{id}")
    @ApiOperation(value = "Update client", notes = "Update client info by Id")
    public Rest<ClientDto> clientUpdate(
            @ApiParam(value = "Client Id") @PathVariable Long id
    ) {

        ClientDto dto = securityTemplate.findClientById(Optional.ofNullable(id).orElse(clientId));
        dto.setSecret("2");
        Set<Long> roles = new HashSet<>();
        roles.add(roleId);
        dto.setRoles(roles);
        securityTemplate.updateClient(dto);

        return RestUtils.operate(HttpStatus.OK.value(), "", dto);
    }

    @GetMapping(value = "/client")
    @ApiOperation(value = "Get clients", notes = "Get client info")
    public Rest<Page<ClientDto>> clientList() {

        ClientDto dto = new ClientDto();
        dto.setEnabled("1");

        return RestUtils.select(securityTemplate.findClient(dto, 1, 10, ""));
    }

    @DeleteMapping(value = "/client/{id}")
    @ApiOperation(value = "Delete client", notes = "Delete client info by Id")
    public Rest clientDelete(
            @ApiParam(value = "Client Id") @PathVariable Long id
    ) {

        securityTemplate.deleteClient(Optional.ofNullable(id).orElse(clientId));
        return RestUtils.delete(true, "");
    }
}
