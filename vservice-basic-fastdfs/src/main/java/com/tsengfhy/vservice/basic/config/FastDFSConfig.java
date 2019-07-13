package com.tsengfhy.vservice.basic.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.domain.conn.ConnectionManager;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.service.TrackerClient;
import com.tsengfhy.vservice.basic.repository.SysFileRepository;
import com.tsengfhy.vservice.basic.template.FileTemplate;
import com.tsengfhy.vservice.basic.template.impl.FastDFSFileTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@ConditionalOnClass({FdfsClientConfig.class})
@AutoConfigureAfter({FdfsClientConfig.class})
public class FastDFSConfig {

    @PostConstruct
    public void init() {
        log.info("VService module [FastDFS] is loaded");
    }

    @Configuration
    @ConditionalOnClass({JpaRepository.class, CacheManager.class})
    @AutoConfigureAfter({JPAConfig.class})
    static class TemplateConfig {

        @Bean
        public FileTemplate fileTemplate(TrackerClient trackerClient, ConnectionManager connectionManager, ThumbImageConfig config, CacheManager cacheManager, SysFileRepository sysFileRepository) throws Exception {
            FastDFSFileTemplate fileTemplate = new FastDFSFileTemplate();

            fileTemplate.setTrackerClient(trackerClient);
            fileTemplate.setConnectionManager(connectionManager);
            fileTemplate.setConfig(config);
            fileTemplate.setCacheManager(cacheManager);
            fileTemplate.setSysFileRepository(sysFileRepository);

            return fileTemplate;
        }
    }
}
