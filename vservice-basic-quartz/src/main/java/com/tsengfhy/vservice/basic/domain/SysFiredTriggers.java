package com.tsengfhy.vservice.basic.domain;

import com.tsengfhy.vservice.basic.domain.key.SysFiredTriggersPK;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_fired_triggers", schema = "sys",
        indexes = {
                @Index(name = "IDX_SYS_FT_TRIG_INST_NAME", columnList = "sched_name, instance_name"),
                @Index(name = "IDX_SYS_FT_INST_JOB_REQ_RCVRY", columnList = "sched_name, instance_name, requests_recovery"),
                @Index(name = "IDX_SYS_FT_J_G", columnList = "sched_name, job_name, job_group"),
                @Index(name = "IDX_SYS_FT_JG", columnList = "sched_name, job_group"),
                @Index(name = "IDX_SYS_FT_T_G", columnList = "sched_name, trigger_name, trigger_group"),
                @Index(name = "IDX_SYS_FT_TG", columnList = "sched_name, trigger_group")
        }
)
public class SysFiredTriggers implements Serializable {

    private static final long serialVersionUID = 3598063063608987717L;

    @Id
    private SysFiredTriggersPK id;

    @Column(name = "trigger_name", length = 190, nullable = false)
    private String triggerName;

    @Column(name = "trigger_group", length = 190, nullable = false)
    private String triggerGroup;

    @Column(name = "instance_name", length = 190, nullable = false)
    private String instanceName;

    @Column(name = "fired_time", length = 13, nullable = false)
    private Long firedTime;

    @Column(name = "sched_time", length = 13, nullable = false)
    private Long schedTime;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "state", length = 16, nullable = false)
    private String state;

    @Column(name = "job_name", length = 190)
    private String jobName;

    @Column(name = "job_group", length = 190)
    private String jobGroup;

    @Column(name = "is_nonconcurrent", length = 1)
    private String isNonconcurrent;

    @Column(name = "requests_recovery", length = 1)
    private String requestsRecovery;
}
