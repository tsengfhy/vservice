package com.tsengfhy.vservice.basic.security.authentication;

import com.tsengfhy.vservice.basic.constant.VerifyCode;
import com.tsengfhy.vservice.basic.security.verify.NullVerifyCodeStrategy;
import com.tsengfhy.vservice.basic.security.verify.VerifyCodeStrategy;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Setter
public class MobileAuthenticationProvider extends AbstractAuthenticationProvider {

    private UserDetailsService userDetailsService;
    private VerifyCodeStrategy verifyCodeStrategy = new NullVerifyCodeStrategy();

    @Override
    protected UserDetails retrieveUser(String username, Authentication authentication) throws AuthenticationException {
        return this.userDetailsService.loadUserByUsername(username);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, Authentication authentication) throws AuthenticationException {
        if (StringUtils.isBlank((String) authentication.getCredentials()) || !verifyCodeStrategy.matches(VerifyCode.LOGIN.name(), (String) authentication.getPrincipal(), (String) authentication.getCredentials())) {
            throw new BadCredentialsException(MessageSourceUtils.getMessage("Security.badCredentials", "Bad credentials"));
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(UserDetails user, Authentication authentication) {
        MobileAuthenticationToken authenticationResult = new MobileAuthenticationToken(user, super.getAuthoritiesMapper().mapAuthorities(user.getAuthorities()));
        authenticationResult.setDetails(authentication.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
