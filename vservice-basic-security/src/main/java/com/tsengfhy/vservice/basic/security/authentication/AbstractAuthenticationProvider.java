package com.tsengfhy.vservice.basic.security.authentication;

import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.util.Assert;

@Setter
public abstract class AbstractAuthenticationProvider implements AuthenticationProvider {

    private UserCache userCache = new NullUserCache();
    @Getter
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
    private boolean hideUserNotFoundExceptions = true;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal() == null ? "NONE_PROVIDED" : authentication.getName();
        boolean cacheWasUsed = true;
        UserDetails user = this.userCache.getUserFromCache(username);
        if (user == null) {
            cacheWasUsed = false;

            try {
                user = this.retrieveUser(username, authentication);
            } catch (UsernameNotFoundException e) {
                if (this.hideUserNotFoundExceptions) {
                    throw new BadCredentialsException(MessageSourceUtils.getMessage("Security.badCredentials", "Bad credentials"));
                }

                throw e;
            }

            Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
        }

        try {
            this.additionalAuthenticationChecks(user, authentication);
            this.userDetailsChecker.check(user);
        } catch (AuthenticationException e) {
            if (!cacheWasUsed) {
                throw e;
            }

            cacheWasUsed = false;
            user = this.retrieveUser(username, authentication);
            this.additionalAuthenticationChecks(user, authentication);
            this.userDetailsChecker.check(user);
        }

        if (!cacheWasUsed) {
            this.userCache.putUserInCache(user);
        }

        return this.createSuccessAuthentication(user, authentication);
    }

    protected abstract UserDetails retrieveUser(String username, Authentication authentication) throws AuthenticationException;

    protected abstract void additionalAuthenticationChecks(UserDetails userDetails, Authentication authentication) throws AuthenticationException;

    protected abstract Authentication createSuccessAuthentication(UserDetails user, Authentication authentication);
}
