package com.tsengfhy.vservice.basic.security.core;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsernamePasswordUserService extends AbstractUserService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return super.transform(username, sysUserRepository.findFirstByUsernameOrMail(username, username));
    }
}
