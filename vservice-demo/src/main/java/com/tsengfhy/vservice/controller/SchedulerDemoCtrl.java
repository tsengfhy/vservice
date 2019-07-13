package com.tsengfhy.vservice.controller;

import com.tsengfhy.vservice.basic.dto.scheduler.SchedulerDto;
import com.tsengfhy.vservice.basic.template.SchedulerTemplate;
import com.tsengfhy.vservice.basic.utils.RestUtils;
import com.tsengfhy.vservice.basic.web.core.Rest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/public/scheduler")
@Api(tags = {"Scheduler Demo"}, description = "A demo of common api for scheduler")
public class SchedulerDemoCtrl {

    @Autowired(required = false)
    private SchedulerTemplate schedulerTemplate;

    @PostMapping(value = "")
    @ApiOperation(value = "Save scheduler", notes = "Save job and auto start then u can see result in console")
    public Rest<SchedulerDto> add() {
        SchedulerDto dto = new SchedulerDto();
        dto.setJobClass("com.tsengfhy.vservice.job.DemoJob");
        dto.setName("demo");
        dto.setGroup("group");
        dto.setDescription("test job");
        dto.setCron("/5 * * * * ?");

        schedulerTemplate.save(dto);

        return RestUtils.save(true, "", dto);
    }

    @PutMapping(value = "")
    @ApiOperation(value = "Update scheduler", notes = "Update job")
    public Rest<SchedulerDto> update() {
        SchedulerDto dto = new SchedulerDto();
        dto.setName("demo");
        dto.setGroup("group");
        dto.setCron("/10 * * * * ?");

        schedulerTemplate.update(dto);

        return RestUtils.update(true, "", dto);
    }

    @PostMapping(value = "/pause")
    @ApiOperation(value = "Pause scheduler", notes = "Pause job")
    public Rest pause() {
        SchedulerDto dto = new SchedulerDto();
        dto.setName("demo");
        dto.setGroup("group");

        schedulerTemplate.pause(dto);

        return RestUtils.operate(HttpStatus.OK.value(), "", null);
    }

    @PostMapping(value = "/resume")
    @ApiOperation(value = "Resume scheduler", notes = "Resume job")
    public Rest resume() {
        SchedulerDto dto = new SchedulerDto();
        dto.setName("demo");
        dto.setGroup("group");

        schedulerTemplate.resume(dto);

        return RestUtils.operate(HttpStatus.OK.value(), "", null);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "Get schedulers", notes = "Get job info")
    public Rest<Page<SchedulerDto>> list() {
        SchedulerDto dto = new SchedulerDto();
        dto.setName("demo");
        dto.setGroup("group");

        Page<SchedulerDto> page = schedulerTemplate.find(dto, 1, 10, "");

        return RestUtils.select(page);
    }

    @DeleteMapping(value = "")
    @ApiOperation(value = "Delete scheduler", notes = "Delete job")
    public Rest delete() {
        SchedulerDto dto = new SchedulerDto();
        dto.setName("demo");
        dto.setGroup("group");

        schedulerTemplate.delete(dto);

        return RestUtils.delete(true, "");
    }
}
