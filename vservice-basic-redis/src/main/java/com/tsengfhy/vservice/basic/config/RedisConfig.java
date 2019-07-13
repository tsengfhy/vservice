package com.tsengfhy.vservice.basic.config;

import com.alibaba.fastjson.JSONAware;
import com.tsengfhy.vservice.basic.properties.Properties;
import com.tsengfhy.vservice.basic.redis.repository.AgileRedisRepositoryFactoryBean;
import com.tsengfhy.vservice.basic.redis.repository.SimpleKeyspaceConfiguration;
import com.tsengfhy.vservice.basic.redis.serializer.FastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@ConditionalOnClass({RedisOperations.class})
@ConditionalOnBean({RedisConnectionFactory.class})
@AutoConfigureAfter({RedisAutoConfiguration.class})
public class RedisConfig {

    @PostConstruct
    public void init() {
        log.info("VService module [Redis] is loaded");
    }

    @ConditionalOnClass({JSONAware.class})
    @DependsOn("redisTemplate")
    @Resource
    public void redisTemplate(RedisTemplate redisTemplate) {

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer();
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
    }


    @Configuration
    @ConditionalOnClass({CacheManager.class, JSONAware.class})
    @ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis", matchIfMissing = true)
    @EnableConfigurationProperties({CacheProperties.class})
    static class CacheConfig extends CachingConfigurerSupport {

        @Autowired
        private Properties properties;

        @Autowired
        private CacheProperties cacheProperties;

        @Autowired
        private RedisConnectionFactory connectionFactory;

        @PostConstruct
        public void init() {
            log.debug("Redis takes over Spring Cache");
        }

        @Override
        public KeyGenerator keyGenerator() {

            return (Object target, Method method, Object... params) -> {

                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(".");
                sb.append(method.getName());
                sb.append("(");
                if (params.length > 0) {
                    sb.append(StringUtils.join(params, ","));
                }
                sb.append(")");

                return sb.toString();
            };
        }

        @Bean
        @Override
        public CacheManager cacheManager() {

            Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<String, RedisCacheConfiguration>();
            List<String> cacheNames = this.cacheProperties.getCacheNames();
            if (!cacheNames.isEmpty()) {
                new LinkedHashSet<>(cacheNames).forEach(item -> {
                    String[] pair = item.split(":");
                    redisCacheConfigurationMap.put(pair[0], redisCacheConfiguration().entryTtl(Duration.ofSeconds(Long.valueOf(pair[1]))));
                });
            }
            redisCacheConfigurationMap.put(
                    com.tsengfhy.vservice.basic.constant.Cache.VERIFY_CODE.name(),
                    redisCacheConfiguration().entryTtl(Duration.ofMillis(properties.getSecurity().getVerifyCode().getExpiredMillis()))
            );
            redisCacheConfigurationMap.put(
                    com.tsengfhy.vservice.basic.constant.Cache.COUNT.name(),
                    redisCacheConfiguration().entryTtl(Duration.ZERO)
            );
            redisCacheConfigurationMap.put(
                    com.tsengfhy.vservice.basic.constant.Cache.UPLOAD.name(),
                    redisCacheConfiguration().entryTtl(Duration.ZERO)
            );

            return new RedisCacheManager(
                    RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory),
                    redisCacheConfiguration().entryTtl(cacheProperties.getRedis().getTimeToLive() != null ? cacheProperties.getRedis().getTimeToLive() : Duration.ZERO),
                    redisCacheConfigurationMap
            );
        }

        private RedisCacheConfiguration redisCacheConfiguration() {
            RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new FastJsonRedisSerializer()));
            if (cacheProperties.getRedis().getKeyPrefix() != null) {
                redisCacheConfiguration = redisCacheConfiguration.prefixKeysWith(cacheProperties.getRedis().getKeyPrefix());
            }
            if (!cacheProperties.getRedis().isCacheNullValues()) {
                redisCacheConfiguration = redisCacheConfiguration.disableCachingNullValues();
            }
            if (!cacheProperties.getRedis().isUseKeyPrefix()) {
                redisCacheConfiguration = redisCacheConfiguration.disableKeyPrefix();
            }

            return redisCacheConfiguration;
        }
    }

    @Configuration
    @ConditionalOnClass({KeyValueRepository.class})
    @ConditionalOnProperty(name = "spring.data.redis.repositories.enabled", havingValue = "true", matchIfMissing = true)
    @EnableAutoConfiguration(exclude = RedisRepositoriesAutoConfiguration.class)
    @EnableRedisRepositories(
            basePackages = "",
            includeFilters = {
                    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = KeyValueRepository.class)
            },
            repositoryFactoryBeanClass = AgileRedisRepositoryFactoryBean.class,
            keyspaceConfiguration = SimpleKeyspaceConfiguration.class
    )
    static class RepositoryConfig {
    }
}
