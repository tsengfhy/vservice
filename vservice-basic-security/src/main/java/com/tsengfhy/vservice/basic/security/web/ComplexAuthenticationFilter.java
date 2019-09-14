package com.tsengfhy.vservice.basic.security.web;

import com.tsengfhy.vservice.basic.properties.Properties;
import com.tsengfhy.vservice.basic.security.authentication.AuthenticationBuilder;
import com.tsengfhy.vservice.basic.security.authentication.NullAuthenticationBuilder;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ComplexAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final RequestMatcher requiresAuthenticationRequestMatcher;

    private final Properties properties;

    @Setter
    private AuthenticationBuilder authenticationBuilder = new NullAuthenticationBuilder();

    public ComplexAuthenticationFilter(Properties properties) {
        super(Optional.ofNullable(properties).map(prop -> prop.getSecurity().getAuthentication().getLoginUrl()).orElse("/login"));

        Assert.notNull(properties, "Properties must not be null!");
        this.properties = properties;

        HttpMethod httpMethod = HttpMethod.resolve(this.properties.getSecurity().getAuthentication().getLimitMethod().toUpperCase());
        if (httpMethod != null) {
            this.requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(this.properties.getSecurity().getAuthentication().getLoginUrl(), httpMethod.name());
        } else {
            this.requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(this.properties.getSecurity().getAuthentication().getLoginUrl());
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!this.requiresAuthenticationRequestMatcher.matches(request)) {
            throw new AuthenticationServiceException(MessageSourceUtils.getMessage("Security.refuseLoginMethod", new Object[]{request.getMethod()}, "Authentication method not support: " + request.getMethod()));
        }

        Authentication authentication = this.authenticationBuilder.build(request);
        ((AbstractAuthenticationToken) authentication).setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authentication);
    }
}
