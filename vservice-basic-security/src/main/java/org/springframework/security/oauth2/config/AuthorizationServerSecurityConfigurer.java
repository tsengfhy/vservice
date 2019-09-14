package org.springframework.security.oauth2.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;
import org.springframework.security.oauth2.provider.error.DefaultOAuth2ExceptionRenderer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AuthorizationServerSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private boolean sslOnly = false;

    private String realm = "oauth2-client";

    private boolean allowFormAuthenticationForClients = false;

    private PasswordEncoder passwordEncoder;

    private OAuth2ExceptionRenderer oAuth2ExceptionRenderer = new DefaultOAuth2ExceptionRenderer();

    private List<Filter> tokenEndpointAuthenticationFilters = new ArrayList<Filter>();

    private String tokenKeyAccess = "denyAll()";

    private String checkTokenAccess = "denyAll()";

    public AuthorizationServerSecurityConfigurer sslOnly() {
        this.sslOnly = true;
        return this;
    }

    public AuthorizationServerSecurityConfigurer realm(String realm) {
        this.realm = realm;
        return this;
    }

    public AuthorizationServerSecurityConfigurer allowFormAuthenticationForClients() {
        this.allowFormAuthenticationForClients = true;
        return this;
    }

    public AuthorizationServerSecurityConfigurer passwordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        return this;
    }

    public AuthorizationServerSecurityConfigurer oAuth2ExceptionRenderer(OAuth2ExceptionRenderer oAuth2ExceptionRenderer) {
        this.oAuth2ExceptionRenderer = oAuth2ExceptionRenderer;
        return this;
    }

    public void addTokenEndpointAuthenticationFilter(Filter filter) {
        this.tokenEndpointAuthenticationFilters.add(filter);
    }

    public void tokenEndpointAuthenticationFilters(List<Filter> filters) {
        Assert.notEmpty(filters, "Authentication filter list must not be null or empty!");
        this.tokenEndpointAuthenticationFilters = new ArrayList<Filter>(filters);
    }

    public AuthorizationServerSecurityConfigurer tokenKeyAccess(String tokenKeyAccess) {
        this.tokenKeyAccess = tokenKeyAccess;
        return this;
    }

    public AuthorizationServerSecurityConfigurer checkTokenAccess(String checkTokenAccess) {
        this.checkTokenAccess = checkTokenAccess;
        return this;
    }

    public String getTokenKeyAccess() {
        return tokenKeyAccess;
    }

    public String getCheckTokenAccess() {
        return checkTokenAccess;
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        registerDefaultAuthenticationEntryPoint(http);

        if (passwordEncoder != null) {
            ClientDetailsUserDetailsService clientDetailsUserDetailsService = new ClientDetailsUserDetailsService(clientDetailsService());
            clientDetailsUserDetailsService.setPasswordEncoder(passwordEncoder());
            http.getSharedObject(AuthenticationManagerBuilder.class)
                    .userDetailsService(clientDetailsUserDetailsService)
                    .passwordEncoder(passwordEncoder());
        } else {
            http.userDetailsService(new ClientDetailsUserDetailsService(clientDetailsService()));
        }

        http
                .httpBasic().realmName(realm).authenticationEntryPoint(authenticationEntryPoint("Basic"))
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .securityContext().securityContextRepository(new NullSecurityContextRepository())
                .and()
                .csrf().disable()
                .logout().disable()
        ;

        if (sslOnly) {
            http.requiresChannel().anyRequest().requiresSecure();
        }
    }

    @SuppressWarnings("unchecked")
    private void registerDefaultAuthenticationEntryPoint(HttpSecurity http) {
        ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling = http.getConfigurer(ExceptionHandlingConfigurer.class);
        if (exceptionHandling == null) {
            return;
        }
        ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(ContentNegotiationStrategy.class);
        if (contentNegotiationStrategy == null) {
            contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
        }
        MediaTypeRequestMatcher preferredMatcher = new MediaTypeRequestMatcher(contentNegotiationStrategy,
                MediaType.APPLICATION_ATOM_XML,
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_OCTET_STREAM,
                MediaType.APPLICATION_XML,
                MediaType.MULTIPART_FORM_DATA,
                MediaType.TEXT_XML);
        preferredMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        exceptionHandling.defaultAuthenticationEntryPointFor(postProcess(authenticationEntryPoint(null)), preferredMatcher);
    }

    private PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return StringUtils.isNotBlank(encodedPassword) ? passwordEncoder.matches(rawPassword, encodedPassword) : true;
            }

            @Override
            public String encode(CharSequence rawPassword) {
                return passwordEncoder.encode(rawPassword);
            }
        };
    }

    private ClientDetailsService clientDetailsService() {
        return getBuilder().getSharedObject(ClientDetailsService.class);
    }

    private AuthenticationEntryPoint authenticationEntryPoint(String typeName) {
        OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        if (StringUtils.isNotBlank(typeName)) {
            authenticationEntryPoint.setTypeName(typeName);
        }
        authenticationEntryPoint.setRealmName(realm);
        authenticationEntryPoint.setExceptionRenderer(oAuth2ExceptionRenderer);
        return authenticationEntryPoint;
    }

    private AccessDeniedHandler accessDeniedHandler() {
        OAuth2AccessDeniedHandler accessDeniedHandler = new OAuth2AccessDeniedHandler();
        accessDeniedHandler.setExceptionRenderer(oAuth2ExceptionRenderer);
        return accessDeniedHandler;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        if (allowFormAuthenticationForClients) {
            clientCredentialsTokenEndpointFilter(http);
        }

        for (Filter filter : tokenEndpointAuthenticationFilters) {
            http.addFilterBefore(filter, BasicAuthenticationFilter.class);
        }
    }

    private ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter(HttpSecurity http) {
        ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter = new ClientCredentialsTokenEndpointFilter(frameworkEndpointHandlerMapping().getServletPath("/oauth/token"));
        clientCredentialsTokenEndpointFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        clientCredentialsTokenEndpointFilter.setAuthenticationEntryPoint(authenticationEntryPoint("Form"));
        clientCredentialsTokenEndpointFilter = postProcess(clientCredentialsTokenEndpointFilter);
        http.addFilterBefore(clientCredentialsTokenEndpointFilter, BasicAuthenticationFilter.class);
        return clientCredentialsTokenEndpointFilter;
    }

    private FrameworkEndpointHandlerMapping frameworkEndpointHandlerMapping() {
        return getBuilder().getSharedObject(FrameworkEndpointHandlerMapping.class);
    }
}