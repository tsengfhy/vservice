package org.springframework.security.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Configuration
public class AuthorizationServerSecurityConfiguration {

    @Autowired
    private List<AuthorizationServerConfigurer> configurers = Collections.emptyList();

    private AuthorizationServerSecurityConfigurer oauthServer = new AuthorizationServerSecurityConfigurer();

    @PostConstruct
    public void init() {
        for (AuthorizationServerConfigurer configurer : configurers) {
            try {
                configurer.configure(oauthServer);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot configure oauthServer", e);
            }
        }
    }

    @Bean
    public AuthorizationServerSecurityConfigurer authorizationServerSecurityConfigurer() {
        return oauthServer;
    }
}
