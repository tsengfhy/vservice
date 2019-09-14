package com.tsengfhy.vservice.basic.security.oauth2;

import com.tsengfhy.vservice.basic.security.authentication.AuthenticationBuilder;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;

public class PasswordTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "password";

    private final AuthenticationBuilder authenticationBuilder;

    private final AuthenticationManager authenticationManager;

    public PasswordTokenGranter(AuthenticationBuilder authenticationBuilder, AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        Assert.notNull(authenticationBuilder, "AuthenticationBuilder must not be null!");
        Assert.notNull(authenticationManager, "AuthenticationManager must not be null!");
        this.authenticationBuilder = authenticationBuilder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

        Authentication authentication = this.authenticationBuilder.build(tokenRequest);
        ((AbstractAuthenticationToken) authentication).setDetails(new LinkedHashMap<String, String>(tokenRequest.getRequestParameters()));
        authentication = authenticationManager.authenticate(authentication);

        if (!authentication.isAuthenticated()) {
            throw new InvalidGrantException(MessageSourceUtils.getMessage("Security.unAuthenticated", new Object[]{authentication.getName()}, "Could not authenticate user: " + authentication.getName()));
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, authentication);
    }
}
