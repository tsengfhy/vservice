package com.tsengfhy.vservice.basic.security.authentication;

import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Setter
public class TokenAuthenticationProvider extends AbstractAuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    protected UserDetails retrieveUser(String username, Authentication authentication) throws AuthenticationException {
        return this.userDetailsService.loadUserByUsername(username);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, Authentication authentication) throws AuthenticationException {

    }

    @Override
    protected Authentication createSuccessAuthentication(UserDetails user, Authentication authentication) {
        TokenAuthenticationToken authenticationResult = new TokenAuthenticationToken(user, super.getAuthoritiesMapper().mapAuthorities(user.getAuthorities()));
        authenticationResult.setDetails(authentication.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
