package com.tsengfhy.vservice.basic.security.core;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MobileUserService extends AbstractUserService {

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        return super.transform(phone, sysUserRepository.findFirstByPhone(phone));
    }
}
