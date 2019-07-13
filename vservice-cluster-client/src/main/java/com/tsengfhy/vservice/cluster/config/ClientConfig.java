package com.tsengfhy.vservice.cluster.config;

import feign.Feign;
import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class ClientConfig {

    @PostConstruct
    public void init() {
        log.info("VService module [Cluster] is loaded");
    }

    @Configuration
    @ConditionalOnClass({DiscoveryClient.class})
    @EnableDiscoveryClient
    static class DiscoveryClientConfig {

    }

    @Configuration
    @ConditionalOnClass({Feign.class})
    @AutoConfigureBefore({FeignAutoConfiguration.class})
    @EnableFeignClients(basePackages = "")
    static class FeignConfig {

        @Bean
        @Profile("dev")
        public Logger.Level feignLoggerLevel() throws Exception {
            return Logger.Level.FULL;
        }
    }
}
