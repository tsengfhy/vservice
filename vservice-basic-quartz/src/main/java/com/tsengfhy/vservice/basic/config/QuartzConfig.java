package com.tsengfhy.vservice.basic.config;

import com.tsengfhy.vservice.basic.repository.SysJobDetailsRepository;
import com.tsengfhy.vservice.basic.template.SchedulerTemplate;
import com.tsengfhy.vservice.basic.template.impl.QuartzSchedulerTemplate;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@ConditionalOnClass({Scheduler.class})
@AutoConfigureAfter({QuartzAutoConfiguration.class})
public class QuartzConfig {

    @PostConstruct
    public void init() {
        log.info("VService module [Quartz] is loaded");
    }

    @Configuration
    @ConditionalOnClass({JpaRepository.class})
    @AutoConfigureAfter({JPAConfig.class})
    static class TemplateConfig {

        @Bean
        public SchedulerTemplate schedulerTemplate(Scheduler scheduler, SysJobDetailsRepository sysJobDetailsRepository) throws Exception {

            QuartzSchedulerTemplate schedulerTemplate = new QuartzSchedulerTemplate();

            schedulerTemplate.setScheduler(scheduler);
            schedulerTemplate.setSysJobDetailsRepository(sysJobDetailsRepository);

            return schedulerTemplate;
        }
    }
}
