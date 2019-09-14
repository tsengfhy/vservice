/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;

/**
 * @author Rob Winch
 * @author Dave Syer
 */
@Configuration
@Order(0)
@Import({AuthorizationServerSecurityConfiguration.class, ClientDetailsServiceConfiguration.class, AuthorizationServerEndpointsConfiguration.class})
public class AuthorizationServerConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerSecurityConfigurer authorizationServerSecurityConfigurer;

    @Autowired
    private AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Over-riding to make sure this.disableLocalConfigureAuthenticationBldr = false
        // This will ensure that when this configurer builds the AuthenticationManager it will not attempt
        // to find another 'Global' AuthenticationManager in the ApplicationContext (if available),
        // and set that as the parent of this 'Local' AuthenticationManager.
        // This AuthenticationManager should only be wired up with an AuthenticationProvider
        // composed of the ClientDetailsService (wired in this configuration) for authenticating 'clients' only.
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.apply(authorizationServerSecurityConfigurer);
        http.setSharedObject(ClientDetailsService.class, clientDetailsService);

        if (!authorizationServerEndpointsConfigurer.isUserDetailsServiceOverride()) {
            UserDetailsService userDetailsService = http.getSharedObject(UserDetailsService.class);
            authorizationServerEndpointsConfigurer.userDetailsService(userDetailsService);
        }
        FrameworkEndpointHandlerMapping handlerMapping = authorizationServerEndpointsConfigurer.getFrameworkEndpointHandlerMapping();
        http.setSharedObject(FrameworkEndpointHandlerMapping.class, handlerMapping);

        String tokenEndpointPath = handlerMapping.getServletPath("/oauth/token");
        String tokenKeyPath = handlerMapping.getServletPath("/oauth/token_key");
        String checkTokenPath = handlerMapping.getServletPath("/oauth/check_token");
        http
                .authorizeRequests()
                .antMatchers(tokenEndpointPath).fullyAuthenticated()
                .antMatchers(tokenKeyPath).access(authorizationServerSecurityConfigurer.getTokenKeyAccess())
                .antMatchers(checkTokenPath).access(authorizationServerSecurityConfigurer.getCheckTokenAccess())
                .and()
                .requestMatchers()
                .antMatchers(tokenEndpointPath, tokenKeyPath, checkTokenPath)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }

}


