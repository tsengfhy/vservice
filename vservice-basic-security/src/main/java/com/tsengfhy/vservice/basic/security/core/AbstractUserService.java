package com.tsengfhy.vservice.basic.security.core;

import com.tsengfhy.vservice.basic.domain.SysGroup;
import com.tsengfhy.vservice.basic.domain.SysUser;
import com.tsengfhy.vservice.basic.dto.security.UserDto;
import com.tsengfhy.vservice.basic.repository.SysUserRepository;
import com.tsengfhy.vservice.basic.repository.SysUserRoleRepository;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractUserService implements UserDetailsService {

    @Autowired
    protected SysUserRepository sysUserRepository;

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    protected User transform(String account, Optional<SysUser> optional) {

        SysUser sysUser = optional.orElseThrow(() -> new UsernameNotFoundException(MessageSourceUtils.getMessage("Security.noUser", new Object[]{account}, "No user with account: " + account)));

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(sysUser, userDto);
        userDto.setGroupId(Optional.ofNullable(sysUser.getSysGroup()).map(SysGroup::getId).orElse(null));
        userDto.getRoles().addAll(sysUserRoleRepository.findAuthorities(userDto.getId()));

        List<GrantedAuthority> authorities = userDto.getRoles().stream().map(role -> new SimpleGrantedAuthority(String.valueOf(role))).collect(Collectors.toList());
        if (authorities.isEmpty()) {
            throw new UsernameNotFoundException(MessageSourceUtils.getMessage("Security.noAuthority", new Object[]{account}, "User " + account + " has no GrantedAuthority"));
        }

        return new User(userDto, authorities);
    }
}
