package com.tsengfhy.vservice.basic.config;

import com.tsengfhy.vservice.basic.core.EnableVService;
import com.tsengfhy.vservice.basic.core.io.YamlPropertySourceFactory;
import com.tsengfhy.vservice.basic.properties.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@PropertySource(value = "classpath:application-vservice.yml", factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
@EnableConfigurationProperties({Properties.class})
@EnableAsync
@EnableCaching
@EnableVService
public class CoreConfig {


    @PostConstruct
    public void init() {
        log.info("VService module [Core] is loaded");
    }

    @RefreshScope
    public Properties properties() {
        return new Properties();
    }
}
