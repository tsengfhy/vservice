package com.tsengfhy.vservice.basic.security.authentication;

import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.provider.TokenRequest;

import javax.servlet.http.HttpServletRequest;

public class NullAuthenticationBuilder implements AuthenticationBuilder {

    @Override
    public AbstractAuthenticationToken build(HttpServletRequest request) throws AuthenticationServiceException {

        throw new AuthenticationServiceException(MessageSourceUtils.getMessage("Security.refuseLogin", "Authentication type not support"));
    }

    @Override
    public AbstractAuthenticationToken build(TokenRequest request) throws InvalidRequestException {

        throw new InvalidRequestException(MessageSourceUtils.getMessage("Security.refuseLogin", "Authentication type not support"));
    }
}
