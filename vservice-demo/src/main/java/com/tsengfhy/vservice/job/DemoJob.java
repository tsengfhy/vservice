package com.tsengfhy.vservice.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

@Slf4j
public class DemoJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Demo job run at {}", new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(LocalDateTime.now()));
    }
}
