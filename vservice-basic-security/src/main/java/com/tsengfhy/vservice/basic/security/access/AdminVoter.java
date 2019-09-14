package com.tsengfhy.vservice.basic.security.access;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

public class AdminVoter implements AccessDecisionVoter<Object> {

    private static final String ADMIN_ROLE_PREFIX = "ROLE_";

    private String role;

    public AdminVoter(String role) {
        Assert.hasText(role, "Admin role must be set!");
        this.role = ADMIN_ROLE_PREFIX + role;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        if (authentication == null) {
            return ACCESS_DENIED;
        } else if (!org.springframework.security.core.userdetails.User.class.isAssignableFrom(authentication.getPrincipal().getClass())
                && !java.lang.String.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            return ACCESS_ABSTAIN;
        }

        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(role::equals)
                .findFirst()
                .map(authority -> ACCESS_GRANTED)
                .orElse(ACCESS_ABSTAIN);
    }
}
