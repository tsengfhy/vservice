package com.tsengfhy.vservice.basic.template.impl;

import com.tsengfhy.vservice.basic.domain.SysCronTriggers;
import com.tsengfhy.vservice.basic.domain.SysJobDetails;
import com.tsengfhy.vservice.basic.domain.key.SysJobDetailsPK;
import com.tsengfhy.vservice.basic.dto.scheduler.SchedulerDto;
import com.tsengfhy.vservice.basic.exception.scheduler.JobNotFoundException;
import com.tsengfhy.vservice.basic.exception.scheduler.NoSuchJobException;
import com.tsengfhy.vservice.basic.exception.scheduler.SchedulerException;
import com.tsengfhy.vservice.basic.repository.SysJobDetailsRepository;
import com.tsengfhy.vservice.basic.template.SchedulerTemplate;
import com.tsengfhy.vservice.basic.utils.DataUtils;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import lombok.Setter;
import org.quartz.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.*;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
public class QuartzSchedulerTemplate implements SchedulerTemplate, InitializingBean {

    private Scheduler scheduler;

    private SysJobDetailsRepository sysJobDetailsRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.scheduler, "Scheduler must not be null!");
        Assert.notNull(this.sysJobDetailsRepository, "SysJobDetailsRepository must not be null!");
    }

    @Override
    public void start() throws SchedulerException {
        try {
            if (scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException("", e);
        }
    }

    @Override
    public void shutdown() throws SchedulerException {
        try {
            if (scheduler.isStarted()) {
                scheduler.shutdown();
            }
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException("", e);
        }
    }

    @Override
    public boolean isExists(SchedulerDto dto) {
        try {
            JobKey jobKey = JobKey.jobKey(dto.getName(), dto.getGroup());

            return scheduler.checkExists(jobKey);
        } catch (org.quartz.SchedulerException e) {
            return false;
        }
    }

    @Override
    public void save(SchedulerDto dto) throws SchedulerException {
        try {
            Class<? extends Job> jobClass;
            try {
                jobClass = Class.forName(dto.getJobClass()).asSubclass(Job.class);
            } catch (ClassNotFoundException e) {
                throw new JobNotFoundException(MessageSourceUtils.getMessage("Scheduler.noJobClass", new Object[]{dto.getJobClass()}, "No job class with name: " + dto.getJobClass()));
            }

            JobKey jobKey = JobKey.jobKey(dto.getName(), dto.getGroup());
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobKey)
                    .withDescription(dto.getDescription())
                    .requestRecovery(dto.isShouldRecover())
                    .storeDurably()
                    .build();

            TriggerKey triggerKey = TriggerKey.triggerKey(dto.getName(), dto.getGroup());
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(dto.getCron()).withMisfireHandlingInstructionDoNothing())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException("", e);
        }
    }

    @Override
    public void update(SchedulerDto dto) throws SchedulerException {
        try {
            if (!isExists(dto)) {
                throw new NoSuchJobException(MessageSourceUtils.getMessage("Scheduler.noJob", new Object[]{JobKey.jobKey(dto.getName(), dto.getGroup())}, "No job with key: " + JobKey.jobKey(dto.getName(), dto.getGroup())));
            }

            TriggerKey triggerKey = TriggerKey.triggerKey(dto.getName(), dto.getGroup());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(dto.getCron()).withMisfireHandlingInstructionDoNothing())
                    .build();

            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException("", e);
        }
    }

    @Override
    public void pause(SchedulerDto dto) throws SchedulerException {
        try {
            if (!isExists(dto)) {
                throw new NoSuchJobException(MessageSourceUtils.getMessage("Scheduler.noJob", new Object[]{JobKey.jobKey(dto.getName(), dto.getGroup())}, "No job with key: " + JobKey.jobKey(dto.getName(), dto.getGroup())));
            }

            JobKey jobKey = JobKey.jobKey(dto.getName(), dto.getGroup());
            scheduler.pauseJob(jobKey);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException("", e);
        }
    }

    @Override
    public void pauseAll() throws SchedulerException {
        try {
            scheduler.pauseAll();
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException("", e);
        }
    }

    @Override
    public void resume(SchedulerDto dto) throws SchedulerException {
        try {
            if (!isExists(dto)) {
                throw new NoSuchJobException(MessageSourceUtils.getMessage("Scheduler.noJob", new Object[]{JobKey.jobKey(dto.getName(), dto.getGroup())}, "No job with key: " + JobKey.jobKey(dto.getName(), dto.getGroup())));
            }

            JobKey jobKey = JobKey.jobKey(dto.getName(), dto.getGroup());
            scheduler.resumeJob(jobKey);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException("", e);
        }
    }

    @Override
    public void resumeAll() throws SchedulerException {
        try {
            scheduler.resumeAll();
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException("", e);
        }
    }

    @Override
    public Page<SchedulerDto> find(SchedulerDto dto, int pageNo, int pageSize, String sort) {

        SysJobDetails sysJobDetails = new SysJobDetails();
        sysJobDetails.setId(new SysJobDetailsPK(null, dto.getName(), dto.getGroup()));
        sysJobDetails.setJobClassName(dto.getJobClass());
        sysJobDetails.setDescription(dto.getDescription());

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Page<SysJobDetails> page = sysJobDetailsRepository.findAll(Example.of(sysJobDetails, matcher), PageRequest.of(pageNo - 1, pageSize, DataUtils.toSort(sort)));
        List<SchedulerDto> list = page.getContent()
                .stream()
                .map(item -> {
                    SchedulerDto schedulerDto = new SchedulerDto();

                    schedulerDto.setJobClass(item.getJobClassName());
                    schedulerDto.setName(item.getId().getJobName());
                    schedulerDto.setGroup(item.getId().getJobGroup());
                    schedulerDto.setDescription(item.getDescription());
                    schedulerDto.setShouldRecover("1".equals(item.getIdDurable()));

                    item.getSysTriggers()
                            .stream()
                            .findFirst()
                            .ifPresent(sysTriggers -> {
                                schedulerDto.setStatus(sysTriggers.getTriggerState());
                                Optional.ofNullable(sysTriggers.getSysCronTriggers()).map(SysCronTriggers::getCronExpression).ifPresent(schedulerDto::setCron);
                            });

                    return schedulerDto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }

    @Override
    public void delete(SchedulerDto dto) throws SchedulerException {
        try {
            if (!isExists(dto)) {
                throw new NoSuchJobException(MessageSourceUtils.getMessage("Scheduler.noJob", new Object[]{JobKey.jobKey(dto.getName(), dto.getGroup())}, "No job with key: " + JobKey.jobKey(dto.getName(), dto.getGroup())));
            }

            JobKey jobKey = JobKey.jobKey(dto.getName(), dto.getGroup());
            scheduler.deleteJob(jobKey);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException("", e);
        }
    }
}
