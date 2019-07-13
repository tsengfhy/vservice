package com.tsengfhy.vservice.basic.template;

import com.tsengfhy.vservice.basic.dto.scheduler.SchedulerDto;
import com.tsengfhy.vservice.basic.exception.scheduler.SchedulerException;
import org.springframework.data.domain.Page;

public interface SchedulerTemplate {

    void start() throws SchedulerException;

    void shutdown() throws SchedulerException;

    boolean isExists(SchedulerDto dto);

    void save(SchedulerDto dto) throws SchedulerException;

    void update(SchedulerDto dto) throws SchedulerException;

    void pause(SchedulerDto dto) throws SchedulerException;

    void pauseAll() throws SchedulerException;

    void resume(SchedulerDto dto) throws SchedulerException;

    void resumeAll() throws SchedulerException;

    Page<SchedulerDto> find(SchedulerDto dto, int pageNo, int pageSize, String sort);

    void delete(SchedulerDto dto) throws SchedulerException;
}
