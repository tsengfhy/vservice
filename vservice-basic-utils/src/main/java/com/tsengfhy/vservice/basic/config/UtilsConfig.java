package com.tsengfhy.vservice.basic.config;

import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.parser.ParserConfig;
import com.tsengfhy.vservice.basic.id.SnowflakeId;
import com.tsengfhy.vservice.basic.properties.Properties;
import com.tsengfhy.vservice.basic.utils.CountUtils;
import com.tsengfhy.vservice.basic.utils.DataUtils;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class UtilsConfig {

    @PostConstruct
    public void init() {
        log.info("VService module [Utils] is loaded");
    }

    @Autowired(required = false)
    public void snowflakeId(Properties properties) {
        SnowflakeId.init(properties.getCluster().getWorkerId(), properties.getCluster().getDataCenterId());
    }

    @Autowired(required = false)
    public void messageSourceUtils(MessageSource messageSource) {
        MessageSourceUtils.setMessageSource(messageSource);
    }

    @Autowired(required = false)
    public void countUtils(CacheManager cacheManager) {
        CountUtils.setCacheManager(cacheManager);
    }

    @Configuration
    @ConditionalOnClass({JSONAware.class})
    static class FastJsonConfig {

        static {
            ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        }
    }

    @Configuration
    @ConditionalOnClass({StringEncryptor.class})
    @EnableEncryptableProperties
    static class JasyptConfig {

        @Autowired(required = false)
        public void dataUtils(StringEncryptor stringEncryptor) {
            DataUtils.setStringEncryptor(stringEncryptor);
        }
    }
}
