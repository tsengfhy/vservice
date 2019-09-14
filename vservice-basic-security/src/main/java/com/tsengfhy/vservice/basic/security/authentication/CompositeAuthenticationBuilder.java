package com.tsengfhy.vservice.basic.security.authentication;

import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompositeAuthenticationBuilder implements AuthenticationBuilder {

    private final List<AuthenticationBuilder> delegateBuilders;

    public CompositeAuthenticationBuilder(List<AuthenticationBuilder> delegateBuilders) {

        this.delegateBuilders = Optional.ofNullable(delegateBuilders)
                .orElseThrow(() -> new IllegalArgumentException("Authentication builder list must not be null!"))
                .stream()
                .peek(authenticationBuilder -> Assert.notNull(authenticationBuilder, "Authentication builder list cannot contain null entries. Got " + delegateBuilders))
                .collect(Collectors.toList());
    }

    @Override
    public AbstractAuthenticationToken build(HttpServletRequest request) throws AuthenticationServiceException {

        return this.delegateBuilders
                .stream()
                .map(authenticationBuilder -> {
                    try {
                        return authenticationBuilder.build(request);
                    } catch (AuthenticationServiceException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new AuthenticationServiceException(MessageSourceUtils.getMessage("Security.refuseLogin", "Authentication type not support")));
    }

    @Override
    public AbstractAuthenticationToken build(TokenRequest request) throws InvalidRequestException {

        return this.delegateBuilders
                .stream()
                .map(authenticationBuilder -> {
                    try {
                        return authenticationBuilder.build(request);
                    } catch (InvalidRequestException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException(MessageSourceUtils.getMessage("Security.refuseLogin", "Authentication type not support")));
    }
}
