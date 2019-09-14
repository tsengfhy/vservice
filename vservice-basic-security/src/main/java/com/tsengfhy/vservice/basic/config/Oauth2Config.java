package com.tsengfhy.vservice.basic.config;

import com.tsengfhy.vservice.basic.properties.Properties;
import com.tsengfhy.vservice.basic.security.authentication.AuthenticationBuilder;
import com.tsengfhy.vservice.basic.security.oauth2.*;
import com.tsengfhy.vservice.basic.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.error.*;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ConditionalOnExpression("'${vservice.security.oauth2.authorization.enabled}'.equals('true') || '${vservice.security.oauth2.resource.enabled}'.equals('true')")
public class Oauth2Config {

    @Autowired
    private Properties properties;

    @Bean
    public AccessTokenConverter accessTokenConverter() throws Exception {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(properties.getArtifact() + "-key");
        return converter;
    }

    @Bean
    public TokenStore tokenStore(AccessTokenConverter accessTokenConverter) throws Exception {
        return new JwtTokenStore((JwtAccessTokenConverter) accessTokenConverter);
    }

    @Bean
    public OAuth2ExceptionRenderer oAuth2ExceptionRenderer() throws Exception {
        return new Oauth2ExceptionRendererImpl();
    }

    @Configuration
    @ConditionalOnProperty(value = "vservice.security.oauth2.authorization.enabled", havingValue = "true")
    @EnableAuthorizationServer
    static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private Properties properties;

        @Autowired
        private OAuth2ExceptionRenderer oAuth2ExceptionRenderer;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private AccessTokenConverter accessTokenConverter;

        @Autowired
        private TokenStore tokenStore;

        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        private AuthenticationBuilder authenticationBuilder;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Value("${spring.application.name:application}")
        private String realm;

        @PostConstruct
        public void init() {
            log.info("VService module [Security] feature [Authorization Server] is loaded");
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {

            oauthServer
                    .addTokenEndpointAuthenticationFilter(clientTokenEndpointAuthenticationFilter())
            ;

            oauthServer
                    .realm(realm)
                    .passwordEncoder(passwordEncoder)
                    .oAuth2ExceptionRenderer(oAuth2ExceptionRenderer)
            ;

            if (properties.getWeb().isSslOnly()) {
                oauthServer.sslOnly();
            }
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients
                    .withClientDetails(new ClientService())
            ;
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .accessTokenConverter(accessTokenConverter)
                    .tokenStore(tokenStore)
                    .userDetailsService(userDetailsService)
                    .tokenGranter(tokenGranter(
                            endpoints.getTokenServices(),
                            endpoints.getClientDetailsService(),
                            endpoints.getOAuth2RequestFactory(),
                            endpoints.getAuthorizationCodeServices()
                    ))
                    .exceptionTranslator(webResponseExceptionTranslator())
            ;
        }

        private ClientTokenEndpointAuthenticationFilter clientTokenEndpointAuthenticationFilter() {
            ClientTokenEndpointAuthenticationFilter clientTokenEndpointAuthenticationFilter = new ClientTokenEndpointAuthenticationFilter(properties);

            return clientTokenEndpointAuthenticationFilter;
        }

        private TokenGranter tokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetails, OAuth2RequestFactory requestFactory, AuthorizationCodeServices authorizationCodeServices) {
            List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
            tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails, requestFactory));
            tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
            tokenGranters.add(new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory));
            tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory));
            tokenGranters.add(new PasswordTokenGranter(authenticationBuilder, authenticationManager, tokenServices, clientDetails, requestFactory));

            return new CompositeTokenGranter(tokenGranters);
        }

        private WebResponseExceptionTranslator webResponseExceptionTranslator() {
            return new DefaultWebResponseExceptionTranslator() {
                @Override
                public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
                    ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);

                    HttpStatus status = responseEntity.getStatusCode();

                    StringBuilder sb = new StringBuilder();
                    sb.append(OAuth2AccessToken.BEARER_TYPE + " ");
                    sb.append("realm=\"" + realm + "\", ");
                    sb.append(e);

                    HttpHeaders update = new HttpHeaders();
                    update.putAll(responseEntity.getHeaders());
                    update.set("WWW-Authenticate", sb.toString());

                    Rest rest = new Rest();
                    rest.setStatus(status.value());
                    rest.setMessage(properties.getWeb().isExceptionFollowed() ? e.getMessage() : status.getReasonPhrase());
                    rest.setPath(WebUtils.getRequest().map(HttpServletRequest::getServletPath).orElse(null));
                    rest.setTimestamp(LocalDateTime.now());

                    return new ResponseEntity<>(rest, update, properties.getWeb().isHttpStatusFollowed() ? status : HttpStatus.OK);
                }
            };
        }
    }

    @Configuration
    @ConditionalOnProperty(value = "vservice.security.oauth2.resource.enabled", havingValue = "true")
    @EnableResourceServer
    static class ResourceServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private Properties properties;

        @Autowired
        private TokenStore tokenStore;

        @Autowired
        private OAuth2ExceptionRenderer oAuth2ExceptionRenderer;

        @Autowired
        private FilterSecurityInterceptor filterSecurityInterceptor;

        @Value("${spring.application.name:application}")
        private String realm;

        @PostConstruct
        public void init() {
            log.info("VService module [Security] feature [Resource Server] is loaded");
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources
                    .resourceId(realm)
                    .tokenStore(tokenStore)
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler())
            ;
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            List<String> publicUrlList = new ArrayList<>();
            publicUrlList.addAll(properties.getSecurity().getAuthentication().getPublicUrls());

            http
                    .logout().disable()
                    .addFilterAt(filterSecurityInterceptor, FilterSecurityInterceptor.class)
                    .authorizeRequests()
                    .antMatchers(publicUrlList.toArray(new String[publicUrlList.size()])).permitAll()
                    .anyRequest().authenticated()
            ;

            if (properties.getWeb().isSslOnly()) {
                http.requiresChannel().anyRequest().requiresSecure();
            }
        }

        private AuthenticationEntryPoint authenticationEntryPoint() {
            OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
            authenticationEntryPoint.setExceptionRenderer(oAuth2ExceptionRenderer);
            authenticationEntryPoint.setRealmName(realm);
            return authenticationEntryPoint;
        }

        private AccessDeniedHandler accessDeniedHandler() {
            OAuth2AccessDeniedHandler accessDeniedHandler = new OAuth2AccessDeniedHandler();
            accessDeniedHandler.setExceptionRenderer(oAuth2ExceptionRenderer);
            return accessDeniedHandler;
        }

    }
}
