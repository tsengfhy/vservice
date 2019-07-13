package com.tsengfhy.vservice.basic.dto.scheduler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Scheduler")
public class SchedulerDto {

    @ApiModelProperty(value = "Class name", position = 1, example = "com.tsengfhy.vservice.job.DemoJob")
    private String jobClass;

    @ApiModelProperty(value = "Name", position = 2)
    private String name;

    @ApiModelProperty(value = "Group", position = 3)
    private String group;

    @ApiModelProperty(value = "Description", position = 4)
    private String description;

    @ApiModelProperty(value = "Cron expression", position = 5, example = "/5 * * * * ?")
    private String cron;

    @ApiModelProperty(value = "Status", position = 6, example = "ACQUIRED")
    private String status;

    @ApiModelProperty(value = "Recover flag", position = 7)
    private boolean shouldRecover;
}
