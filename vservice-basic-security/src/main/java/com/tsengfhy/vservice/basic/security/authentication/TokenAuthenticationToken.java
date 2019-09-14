package com.tsengfhy.vservice.basic.security.authentication;

import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TokenAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 0L;
    private final Object principal;

    public TokenAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        this.setAuthenticated(false);
    }

    public TokenAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(MessageSourceUtils.getMessage("Security.refuseAuthenticated", "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead"));
        } else {
            super.setAuthenticated(false);
        }
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
