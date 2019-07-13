package com.tsengfhy.vservice.cluster.config;

import com.tsengfhy.vservice.cluster.server.gateway.CascadeServiceRouteMapper;
import com.tsengfhy.vservice.cluster.server.gateway.DefaultFallbackProvider;
import com.tsengfhy.vservice.cluster.server.gateway.SendErrorExtFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.cloud.netflix.zuul.filters.post.LocationRewriteFilter;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class ServerConfig {

    @Configuration
    @ConditionalOnClass(EnableEurekaServer.class)
    @Profile("discovery")
    @EnableEurekaServer
    static class DiscoveryConfig {

        @PostConstruct
        public void init() {
            log.info("VService module [Cluster] feature [Discovery Server] is loaded");
        }
    }

    @Configuration
    @ConditionalOnClass(EnableConfigServer.class)
    @Profile("config")
    @EnableConfigServer
    static class ConfigConfig {

        @PostConstruct
        public void init() {
            log.info("VService module [Cluster] feature [Config Server] is loaded");
        }
    }

    @Configuration
    @ConditionalOnClass(EnableZuulProxy.class)
    @Profile("gateway")
    @EnableZuulProxy
    static class GatewayConfig {

        @PostConstruct
        public void init() {
            log.info("VService module [Cluster] feature [Gateway Server] is loaded");
        }

        @RefreshScope
        @ConfigurationProperties("zuul")
        public ZuulProperties zuulProperties() throws Exception {
            return new ZuulProperties();
        }

        @Bean
        @ConditionalOnProperty(value = "zuul.LocationRewriteFilter.error.disable", havingValue = "false", matchIfMissing = true)
        public LocationRewriteFilter locationRewriteFilter() throws Exception {
            return new LocationRewriteFilter();
        }

        @Bean
        @ConditionalOnProperty(value = "zuul.SendErrorFilter.error.disable", havingValue = "false", matchIfMissing = true)
        public SendErrorExtFilter sendErrorExtFilter() throws Exception {
            return new SendErrorExtFilter();
        }

        @Bean
        public ServiceRouteMapper serviceRouteMapper() throws Exception {
            return new CascadeServiceRouteMapper();
        }

        @Bean
        public FallbackProvider fallbackProvider() throws Exception {
            return new DefaultFallbackProvider();
        }
    }
}
