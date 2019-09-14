package com.tsengfhy.vservice.basic.security.core;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class TokenUserService extends AbstractUserService {

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        return super.transform(token, sysUserRepository.findFirstByToken(token));
    }
}
