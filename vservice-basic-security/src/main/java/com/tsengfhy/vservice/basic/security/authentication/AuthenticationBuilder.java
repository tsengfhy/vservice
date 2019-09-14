package com.tsengfhy.vservice.basic.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.provider.TokenRequest;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationBuilder {

    AbstractAuthenticationToken build(HttpServletRequest request) throws AuthenticationServiceException;

    AbstractAuthenticationToken build(TokenRequest request) throws InvalidRequestException;
}
